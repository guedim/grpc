package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"os"
	"os/signal"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/reflection"
	"google.golang.org/grpc/status"

	"grpc-greeting/blog/blogpb"
)

var collection *mongo.Collection

type server struct{}

type blogItem struct {
	ID       primitive.ObjectID `bson:"_id,omitempty"`
	AuthorID string             `bson:"author_id"`
	Content  string             `bson:"content"`
	Title    string             `bson:"title"`
}

func dataToBlogpb(data blogItem) *blogpb.Blog {
	return &blogpb.Blog{
		Id:       data.ID.Hex(),
		AuthorId: data.AuthorID,
		Content:  data.Content,
		Title:    data.Title,
	}
}

func (*server) CreateBlog(ctx context.Context, req *blogpb.CreateBlogRequest) (*blogpb.CreateBlogResponse, error) {
	fmt.Println("Created blog request !!!")
	blog := req.GetBlog()
	data := blogItem{
		AuthorID: blog.GetAuthorId(),
		Content:  blog.GetContent(),
		Title:    blog.GetTitle(),
	}
	res, err := collection.InsertOne(ctx, data)
	if err != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("Internal error: %v", err))
	}
	oid, ok := res.InsertedID.(primitive.ObjectID)
	if !ok {
		return nil, status.Errorf(codes.Internal, "Cannot convert to OID")
	}

	return &blogpb.CreateBlogResponse{
		Blog: &blogpb.Blog{
			Id:       oid.Hex(),
			AuthorId: blog.GetAuthorId(),
			Title:    blog.GetTitle(),
			Content:  blog.GetContent(),
		},
	}, nil

}

func (*server) ReadBlog(ctx context.Context, req *blogpb.ReadBlogRequest) (*blogpb.ReadBlogResponse, error) {
	fmt.Println("Read blog request !!!")
	blogId := req.GetBlogId()
	oid, err := primitive.ObjectIDFromHex(blogId)
	if err != nil {
		return nil, status.Errorf(codes.InvalidArgument, fmt.Sprintf("cannot parse Id : %v", err))
	}

	// create an empty struct
	data := &blogItem{}
	filter := bson.D{primitive.E{Key: "_id", Value: oid}}
	res := collection.FindOne(context.Background(), filter)
	if err := res.Decode(data); err != nil {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("Cannot find blog with specified id: %v", err))
	}
	return &blogpb.ReadBlogResponse{Blog: dataToBlogpb(*data)}, nil
}

func (*server) UpdateBlog(ctx context.Context, req *blogpb.UpdateBlogRequest) (*blogpb.UpdateBlogResponse, error) {
	fmt.Println("Update  blog request !!!")
	blog := req.GetBlog()
	oid, err := primitive.ObjectIDFromHex(blog.GetId())
	if err != nil {
		return nil, status.Errorf(codes.InvalidArgument, fmt.Sprintf("cannot parse Id : %v", err))
	}

	// create an empty struct
	data := &blogItem{}
	filter := bson.D{primitive.E{Key: "_id", Value: oid}}
	res := collection.FindOne(context.Background(), filter)
	if err := res.Decode(data); err != nil {
		return nil, status.Errorf(codes.NotFound, fmt.Sprintf("Cannot find blog with specified id: %v", err))
	}

	// we update our internal struct
	data.AuthorID = blog.GetAuthorId()
	data.Content = blog.GetContent()
	data.Title = blog.GetTitle()

	_, err2 := collection.ReplaceOne(ctx, filter, data)
	if err2 != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("Cannot update blog with specified id: %v", err2))
	}

	return &blogpb.UpdateBlogResponse{Blog: dataToBlogpb(*data)}, nil
}

func (*server) DeleteBlog(ctx context.Context, req *blogpb.DeleteBlogRequest) (*blogpb.DeleteBlogResponse, error) {
	fmt.Println("Delete  blog request !!!")
	blogId := req.GetBlogId()
	oid, err := primitive.ObjectIDFromHex(blogId)
	if err != nil {
		return nil, status.Errorf(codes.InvalidArgument, fmt.Sprintf("cannot parse Id : %v", err))
	}

	// delete blog by id
	filter := bson.D{primitive.E{Key: "_id", Value: oid}}
	_, err2 := collection.DeleteOne(ctx, filter)
	if err2 != nil {
		return nil, status.Errorf(codes.Internal, fmt.Sprintf("Cannot delete  blog with specified id: %v", err2))
	}

	return &blogpb.DeleteBlogResponse{BlogId: blogId}, nil
}

func (*server) ListBlog(req *blogpb.ListBlogRequest, stream blogpb.BlogService_ListBlogServer) error {
	fmt.Println("List  blog request !!!")
	filter := bson.D{{}}
	cur, err := collection.Find(context.Background(), filter)
	if err != nil {
		return status.Errorf(codes.Internal, fmt.Sprintf("Unknow internal error: %v", err))
	}

	defer cur.Close(context.Background())

	for cur.Next(context.Background()) {
		data := &blogItem{}
		err := cur.Decode(data)
		if err != nil {
			return status.Errorf(codes.Internal, fmt.Sprintf("Error while decoding data from mongoDb: %v", err))
		}
		stream.Send(&blogpb.ListBlogResponse{Blog: dataToBlogpb(*data)})
		time.Sleep(1 * time.Second)
	}

	if cur.Err(); err != nil {
		return status.Errorf(codes.Internal, fmt.Sprintf("Error while closing cursor  from mongoDb: %v", err))
	}
	return nil
}

func main() {

	// if we creash the go code, we get the file name and line number
	log.SetFlags(log.LstdFlags | log.Lshortfile)

	// Connect to MongoDB
	fmt.Println("Connecting to MongoDB ...")
	clientOptions := options.Client().ApplyURI("mongodb://localhost:27017")
	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Fatal(err)
	}
	// Check the connection
	err = client.Ping(context.TODO(), nil)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("Connected to MongoDB!")
	collection = client.Database("myGoDb").Collection("blog")

	fmt.Println("Starting Blog server ...")
	list, err := net.Listen("tcp", "localhost:50051")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}

	opts := []grpc.ServerOption{}
	s := grpc.NewServer(opts...)
	blogpb.RegisterBlogServiceServer(s, &server{})
	// Register reflection service on GRPC
	reflection.Register(s)

	go func() {
		fmt.Println("Starting Server...")
		if err := s.Serve(list); err != nil {
			log.Fatalf("Failed to server: %v", err)
		}
	}()

	// Wait for Control C for exit
	ch := make(chan os.Signal, 1)
	signal.Notify(ch, os.Interrupt)

	// Block until a signal is received
	<-ch
	fmt.Println("Stopping the server")
	s.Stop()
	fmt.Println("Stopping the listener")
	list.Close()
	fmt.Println("Closing MogoDb connection")
	client.Disconnect(context.TODO())
	fmt.Println("End of Program")

}

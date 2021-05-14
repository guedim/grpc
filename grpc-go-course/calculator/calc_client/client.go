package main

import (
	"context"
	"fmt"
	"grpc-greeting/calculator/calcpb"
	"io"
	"log"
	"time"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func main() {

	fmt.Println("Hello I am a client")

	conn, err := grpc.Dial("localhost:50051", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("could not connect: %v", err)
	}
	defer conn.Close()

	client := calcpb.NewSumServiceClient(conn)

	//doUnary(client)
	//doServerStreaming(client)
	//doClientStreaming(client)
	//doBidiStreaming(client)
	doErrorUnary(client)

}

func doUnary(c calcpb.SumServiceClient) {

	fmt.Println("Starting to do an unary RPC...")
	req := &calcpb.SumRequest{
		Sum: &calcpb.Sum{
			FirstNumber:  7,
			SecondNumber: 8,
		},
	}
	resp, err := c.Add(context.Background(), req)
	if err != nil {
		log.Fatalf("error while calling Add RPC: %v", err)
	}

	log.Printf("Response from Greet: %v", resp.Result)
}

func doServerStreaming(c calcpb.SumServiceClient) {
	fmt.Println("Starting to do an streaming RPC...")
	req := &calcpb.PrimeRequest{
		PrimeNumber: 120,
	}
	resStream, err := c.PrimeNumberDescomposition(context.Background(), req)

	if err != nil {
		log.Fatalf("error while calling Prime Number Descomposition RPC: %v", err)
	}

	for {
		msg, err := resStream.Recv()
		if err == io.EOF {
			// we have reached end of file
			break
		}
		if err != nil {
			log.Fatalf("error while reading streaming greet many times: %v", err)
		}
		log.Printf("Response from GreetManyTimes: %v", msg.GetResult())
	}
}

func doClientStreaming(c calcpb.SumServiceClient) {
	fmt.Println("Starting to do a compute average client streaming RPC...")

	requests := []*calcpb.NumberRequest{
		{
			Number: 1,
		},
		{
			Number: 2,
		},
		{
			Number: 3,
		},
		{
			Number: 4,
		},
	}

	stream, err := c.ComputeAverage(context.Background())
	if err != nil {
		log.Fatalf("Error while calling ComputeAverage: %v", err)
	}
	// we iterate over slice requests and send each message individually
	for _, req := range requests {
		fmt.Printf("Sending request: %v\n", req)
		stream.Send(req)
		time.Sleep(1 * time.Second)
	}

	res, err := stream.CloseAndRecv()
	if err != nil {
		log.Fatalf("Error while receiving response form ComputeAverage: %v", err)
	}
	fmt.Printf("ComputeAverage response: %v\n", res.GetAverage())
}

func doBidiStreaming(c calcpb.SumServiceClient) {
	fmt.Println("Starting to do a bidi client streaming RPC...")

	// Create a stream by invoking the client
	stream, err := c.FindMaximum(context.Background())
	if err != nil {
		log.Fatalf("Error while creating a stream: %v", err)
		return
	}

	waitc := make(chan struct{})
	// Send a bunch of messages to the client (go routine)
	go func() {
		// function to send a bunch of messages
		numbers := []int32{4, 7, 2, 19, 4, 6, 32}
		for _, number := range numbers {
			fmt.Printf("Sending number: %v\n", number)
			stream.Send(&calcpb.FindMaximumRequest{Number: number})
			time.Sleep(1 * time.Second)
		}
		stream.CloseSend()
	}()

	// Receive a bunch of messages fron the client (go routine)
	go func() {
		// function to send a bunch of messages
		for {
			res, err := stream.Recv()

			if err == io.EOF {
				break
			}
			if err != nil {
				log.Fatalf("Error while reading server stream: %v", err)
				break
			}
			max := res.GetMaximum()
			fmt.Printf("Receiveda new maximun of: %v\n", max)
		}
		close(waitc)
	}()

	// Block until everything is done
	<-waitc
}

func doErrorUnary(c calcpb.SumServiceClient) {
	fmt.Println("Starting to do a doErrorUnary client RPC...")
	// correct call
	doErrorCall(c, 10)

	// error call
	doErrorCall(c, -2)

}

func doErrorCall(c calcpb.SumServiceClient, n int32) {

	res, err := c.SquareRoot(context.Background(), &calcpb.SquareRootRequest{Number: n})

	if err != nil {
		respErr, ok := status.FromError(err)
		if ok {
			// Actual error from gRPC (user error)
			fmt.Printf("Error message from server: %v\n", respErr.Message())
			fmt.Printf("Error code from server: %v\n", respErr.Code())
			if respErr.Code() == codes.InvalidArgument {
				fmt.Println("We probably sent a negative number!\n")
				return
			}
		} else {
			log.Fatalf("Big Error calling SquareRoot: %v", err)
			return
		}
	}
	fmt.Printf("Result of square root of %v: %v\n", n, res.GetNumberRoot())

}

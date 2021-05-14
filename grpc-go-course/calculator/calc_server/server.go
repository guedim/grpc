package main

import (
	"context"
	"fmt"
	"io"
	"log"
	"math"
	"net"
	"time"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	"grpc-greeting/calculator/calcpb"

	"google.golang.org/grpc/reflection"
)

type server struct {
}

func (*server) Add(ctx context.Context, req *calcpb.SumRequest) (*calcpb.SumResponse, error) {
	fmt.Printf("Add function was invoked with %v\n", req)
	fistNumber := req.GetSum().GetFirstNumber()
	secondNumber := req.GetSum().GetSecondNumber()
	result := fistNumber + secondNumber

	res := &calcpb.SumResponse{
		Result: result,
	}

	return res, nil
}

func (*server) PrimeNumberDescomposition(req *calcpb.PrimeRequest, stream calcpb.SumService_PrimeNumberDescompositionServer) error {
	fmt.Printf("PrimeNumberDescomposition function was invoked with %v\n", req)
	k := int32(2)
	N := req.GetPrimeNumber()

	for N > 1 {
		if N%k == 0 {
			res := calcpb.PrimeResponse{
				Result: k,
			}
			stream.Send(&res)
			time.Sleep(500 * time.Millisecond)
			N = N / k
		} else {
			k = k + 1
		}
	}
	return nil
}

func (*server) ComputeAverage(stream calcpb.SumService_ComputeAverageServer) error {
	fmt.Printf("ComputeAverage function was invoked with a streaming request\n")
	sum := int32(0)
	count := 0
	for {
		req, err := stream.Recv()
		if err == io.EOF {
			// we have finished reading the client stream
			average := float64(sum) / float64(count)
			return stream.SendAndClose(&calcpb.ComputedResponse{
				Average: average,
			})
		}
		if err != nil {
			log.Fatalf("Error while reading client stream: %v", err)
		}
		sum += req.GetNumber()
		count++
	}
}

func (*server) FindMaximum(stream calcpb.SumService_FindMaximumServer) error {
	fmt.Printf("FindMaximum function was invoked with a streaming request\n")
	max := int32(0)

	for {
		req, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			log.Fatalf("Error while reading client stream: %v", err)
			return err
		}
		number := req.GetNumber()
		// Find max number
		if number > max {
			max = number
			sendErr := stream.Send(&calcpb.FindMaximumResponse{
				Maximum: max,
			})
			if sendErr != nil {
				log.Fatalf("Error while sending data to client: %v", err)
				return err
			}
		}
	}
}

func (*server) SquareRoot(ctx context.Context, req *calcpb.SquareRootRequest) (*calcpb.SquareRootResponse, error) {
	fmt.Printf("Received SquareRoot request\n")
	number := req.GetNumber()
	if number < 0 {
		return nil, status.Errorf(
			codes.InvalidArgument,
			fmt.Sprintf("Received a negative number: %v", number),
		)
	}
	return &calcpb.SquareRootResponse{
		NumberRoot: math.Sqrt(float64(number)),
	}, nil
}

func main() {
	fmt.Println("Starting sum server...")

	list, err := net.Listen("tcp", "0.0.0.0:50051")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}

	s := grpc.NewServer()
	calcpb.RegisterSumServiceServer(s, &server{})

	// Registrer reflection on gRPC server
	reflection.Register(s)

	if err := s.Serve(list); err != nil {
		log.Fatalf("Failed to server: %v", err)
	}
}

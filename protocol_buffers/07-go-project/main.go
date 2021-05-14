package main

import (
	"fmt"
	"io/ioutil"
	"log"

	"github.com/guedim/07-go-project/src/simple"
	"google.golang.org/protobuf/proto"
)

func main() {
	fmt.Println("Hello World")

	sm := doSimple()

	readAndWrite(sm)

}

func readAndWrite(sm proto.Message) {
	writeToFile("simple.bin", sm)

	sm2 := &simple.SimpleMessage{}
	readFromFile("simple.bin", sm2)
	fmt.Println("Read the content:", sm2)
}

func writeToFile(fileName string, pb proto.Message) error {
	out, err := proto.Marshal(pb)
	if err != nil {
		log.Fatalln("Cannot write to file", err)
		return err
	}

	err2 := ioutil.WriteFile(fileName, out, 0644)
	if err != nil {
		log.Fatalln("Cannot write to file", err2)
		return err
	}

	fmt.Println("data has been written !!!")
	return nil
}

func readFromFile(fileName string, pb proto.Message) error {

	in, err := ioutil.ReadFile(fileName)
	if err != nil {
		log.Fatalln("Something went wrong when reading the file", err)
		return err
	}

	err2 := proto.Unmarshal(in, pb)
	if err2 != nil {
		log.Fatalln("Couldn't put the bytes into the protocol buffers struct", err2)
		return err2
	}

	return nil
}

func doSimple() *simple.SimpleMessage {
	sm := simple.SimpleMessage{
		Id:         12345,
		IsSimple:   true,
		Name:       "Mario",
		SampleList: []int32{1, 2, 3, 4, 5},
	}

	fmt.Println(&sm)
	return &sm
}

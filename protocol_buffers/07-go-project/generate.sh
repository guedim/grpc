# Create module
go mod init github.com/guedim/07-go-project

# Generate source code
protoc -I src/ --go_out=src/ src/simple/simple.proto

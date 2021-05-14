import simple.simple_pb2 as simple_pb

simple_message = simple_pb.SimpleMessage()
simple_message.id = 123
simple_message.is_simple = True
simple_message.name = "This is a simple message"

simple_list = simple_message.sample_list
simple_list.append(1)
simple_list.append(2)
simple_list.append(3)

print(simple_message)


with open("simple.bin", "wb") as f:
    print("write as binary")
    bytesAsString = simple_message.SerializeToString()
    f.write(bytesAsString)

with open("simple.bin", "rb") as f:
    print("read values")
    simple_message_read = simple_pb.SimpleMessage().FromString(f.read())

print(simple_message_read)
print("is simple: " + str(simple_message_read.is_simple))
print("name: " + str(simple_message_read.name))
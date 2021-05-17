package com.guedim.model.protobf;


import com.google.protobuf.Int32Value;
import com.guedim.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.*;

public class PersonDemo {
    public static void main(String[] args) throws IOException {

        Person mati = Person.newBuilder()
                .setName("Matines")
                .setAge(Int32Value.newBuilder().setValue(4).build())
                .build();

        // write person to file
        Path path =  Paths.get("mati.txt");
        write(path, mati.toByteArray());

        // read person from file
        byte[] bytes =  Files.readAllBytes(path);
        Person newMati = Person.parseFrom(bytes);
        System.out.println(newMati);

    }
}

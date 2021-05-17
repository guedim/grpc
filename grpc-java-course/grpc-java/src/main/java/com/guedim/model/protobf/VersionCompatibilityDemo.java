package com.guedim.model.protobf;

import com.guedim.model.Television;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VersionCompatibilityDemo {

    public static void main(String[] args) throws IOException {


        Path pathV1 = Paths.get("tv-v1.txt");
        Path pathV2 = Paths.get("tv-v2.txt");

        /*Television television = Television.newBuilder()
                .setBrand("LG")
                .setModel(2021)
                .setType(Type.OLED)
                .build();

        // Serialize
        Files.write(pathV2, television.toByteArray());*/

        // Deserialize
        byte[] bytes = Files.readAllBytes(pathV1);
        System.out.println(Television.parseFrom(bytes));
    }
}

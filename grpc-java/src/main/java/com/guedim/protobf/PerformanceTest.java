package com.guedim.protobf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.guedim.json.JsonPerson;
import com.guedim.model.Person;

public class PerformanceTest {

    public static void main(String[] args) {

        // json
        JsonPerson personJson = new JsonPerson();
        personJson.setName("Mati");
        personJson.setAge(4);
        ObjectMapper mapper = new ObjectMapper();
        Runnable runnable1 = () -> {
            try {
                byte[] bytes =  mapper.writeValueAsBytes(personJson);
                JsonPerson person1 =  mapper.readValue(bytes, JsonPerson.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // protobf
        Person personPb = Person.newBuilder().setName("Mati").setAge(4).build();
        Runnable runnable2 = () -> {
            try {
                byte[] bytes = personPb.toByteArray();
                Person person1 =  Person.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 10; i++) {
            runPerformanceTest(runnable1, "json");
            runPerformanceTest(runnable2, "pb");
        }
    }

    private static void runPerformanceTest(Runnable runnable, String method) {
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 5_000_000; i++) {
            runnable.run();
        }
        long time2 = System.currentTimeMillis();
        System.out.println(method + ":" + (time2 - time1) + " ms.");
    }
}
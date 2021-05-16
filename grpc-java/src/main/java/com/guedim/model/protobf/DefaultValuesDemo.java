package com.guedim.model.protobf;

import com.guedim.model.Person;

public class DefaultValuesDemo {

    public static void main(String[] args) {

        Person defaultPerson = Person.newBuilder().build();

        System.out.println(defaultPerson);
        System.out.println("City:" + defaultPerson.getAddress().getCity());
        System.out.println("has Address:" + defaultPerson.hasAddress());
    }
}

package com.guedim.model.protobf;

import com.guedim.model.Address;
import com.guedim.model.Car;
import com.guedim.model.Person;

public class PersonCompositionDemo {

    public static void main(String[] args) {

        Address address = Address.newBuilder()
                .setPostbox(123)
                .setStreet("22")
                .setCity("Bogotá")
                .build();

        Car car = Car.newBuilder()
                .setMake("Renailt")
                .setModel("Clio")
                .setYear(2015)
                .build();

        Person person = Person.newBuilder()
                .setName("Matías")
                .setAge(4)
                .setCar(car)
                .setAddress(address)
                .build();

    }
}

package com.guedim.model.protobf;

import com.guedim.model.Address;
import com.guedim.model.Car;
import com.guedim.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonCompositionDemo {

    public static void main(String[] args) {

        Address address = Address.newBuilder()
                .setPostbox(123)
                .setStreet("22")
                .setCity("Bogotá")
                .build();

        Car clio = Car.newBuilder()
                .setMake("Renault")
                .setModel("Clio")
                .setYear(2015)
                .build();

        Car spark = Car.newBuilder()
                .setMake("Chevrolet")
                .setModel("Spark")
                .setYear(2015)
                .build();

        List<Car> cars = new ArrayList<>();
        cars.add(clio);
        cars.add(spark);

        Person person = Person.newBuilder()
                .setName("Matías")
                .setAge(4)
                //.addCar(clio)
                //.addCar(spark)
                .addAllCar(cars)
                .setAddress(address)
                .build();

        System.out.println(person);

    }
}

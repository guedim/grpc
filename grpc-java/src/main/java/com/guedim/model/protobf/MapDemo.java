package com.guedim.model.protobf;

import com.guedim.model.BodyStyle;
import com.guedim.model.Car;
import com.guedim.model.Dealer;

public class MapDemo {
    public static void main(String[] args) {

        Car clio = Car.newBuilder()
                .setMake("Renault")
                .setModel("Clio")
                .setYear(2015)
                .setBodyStyle(BodyStyle.COUPE)
                .build();

        Car spark = Car.newBuilder()
                .setMake("Chevrolet")
                .setModel("Spark")
                .setYear(2015)
                .setBodyStyle(BodyStyle.SUV)
                .build();

        Dealer dealer = Dealer.newBuilder()
                .putModel(2015, clio)
                .putModel(2016, spark)
                .build();

        System.out.println(dealer.getModelOrDefault(2020, clio));

        System.out.println(dealer.getModelMap());

        System.out.println(dealer.getModelOrThrow(2015).getBodyStyle());

    }
}

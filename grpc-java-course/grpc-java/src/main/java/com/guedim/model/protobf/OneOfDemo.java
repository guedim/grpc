package com.guedim.model.protobf;

import com.guedim.model.Credentials;
import com.guedim.model.EmailCredentials;
import com.guedim.model.PhoneOTP;

public class OneOfDemo {

    public static void main(String[] args) {

        EmailCredentials emailCredentials = EmailCredentials.newBuilder()
                .setEmailAddress("matico@gmail.com")
                .setPassword("12345678")
                .build();

        PhoneOTP phomeCredentials = PhoneOTP.newBuilder()
                .setNumber(3156893)
                .setCode(123)
                .build();

        Credentials credentials = Credentials.newBuilder()
                .setPhoneMode(phomeCredentials)
                .setEmailMode(emailCredentials)
                .build();

        login(credentials);
    }

    private static void login(Credentials credentials) {

        switch (credentials.getModeCase()) {
            case EMAIL_MODE:
                System.out.println(credentials.getEmailMode());
                break;
            case PHONE_MODE:
                System.out.println(credentials.getPhoneMode());
                break;
        }


    }
}

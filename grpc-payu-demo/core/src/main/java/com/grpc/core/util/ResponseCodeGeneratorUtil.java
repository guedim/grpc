package com.grpc.core.util;

import java.util.Random;

public final class ResponseCodeGeneratorUtil {

    private static Random rand = new Random();
    private static int upperbound = 100;

    /**
     * Generate a random string between 0 and 99
     * @return
     */
    public static String generateResponseCode() {
        return String.valueOf(rand.nextInt(upperbound));
    }
}

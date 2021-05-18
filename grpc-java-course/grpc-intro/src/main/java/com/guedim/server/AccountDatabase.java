package com.guedim.server;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDatabase {

    /*
        This a DB
            - Account 1 with USD $ 10.
            - Account 2 with USD $ 30.
            ...
            - Account 100 with USD $ 100.
     */
    private static final Map<Integer, Integer> MAP = IntStream
            .rangeClosed(1,10)
            .boxed()
            .collect(Collectors.toMap(Function.identity(), v -> v * 10));


    public static Integer getBalance (int accountId){
        return MAP.getOrDefault (accountId, 0);
    }

    public static Integer addBalance(int accountId, int amount) {
        return  MAP.computeIfPresent(accountId, (k,v) -> v + amount);
    }

    public static Integer deductBalance (int accountId, int amount) {
        return  MAP.computeIfPresent(accountId, (k,v) -> v - amount);
    }
}

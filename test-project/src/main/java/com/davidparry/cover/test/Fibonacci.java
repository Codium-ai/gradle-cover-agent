package com.davidparry.cover.test;

public class Fibonacci {

    public int calculate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Input should be a positive integer.");
        }

        if (n == 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }

        int a = 0, b = 1;
        for (int i = 3; i <= n; i++) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

}

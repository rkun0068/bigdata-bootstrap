package com.example.memory;

public class ParamOrder {
    public static void add(int x, double y, String z) {
        int local = 10;
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
    }

    public static void main(String[] args) {
        add(5, 3.14, "hello");
    }
}

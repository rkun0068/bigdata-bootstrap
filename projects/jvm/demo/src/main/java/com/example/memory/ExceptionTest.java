package com.example.memory;

public class ExceptionTest {
    public void test() {
        try {
            int a = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic error");
        } catch (Exception e) {
            System.out.println("General error");
        } finally {
            System.out.println("Finally block");
        }
    }

    public static void main(String[] args) {
        ExceptionTest test = new ExceptionTest();
        test.test();
    }
}

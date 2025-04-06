package com.example.memory;
public class StackOverflowTest {
    // 用于统计调用次数
    private static int callCount = 0;

    public static void recursiveCall() {
        callCount++;
        recursiveCall(); // 递归调用，无退出条件
    }

    public static void main(String[] args) {
        try {
            recursiveCall();
        } catch (StackOverflowError e) {
            System.out.println("Stack overflow after " + callCount + " calls.");
            e.printStackTrace();
        }
    }
}

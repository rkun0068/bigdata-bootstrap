package com.example.memory;
import java.util.ArrayList;
import java.util.List;

public class HeapOverflowTest {

    static class BigObject {
        // 让对象占据更多内存
        private byte[] memory = new byte[1024 * 1024]; // 每个对象大约 1MB
    }

    public static void main(String[] args) {
        List<BigObject> list = new ArrayList<>();
        int count = 0;

        try {
            while (true) {
                list.add(new BigObject());
                count++;
                System.out.println("Created object #" + count);
            }
        } catch (OutOfMemoryError e) {
            System.out.println("🔥 OutOfMemoryError occurred after creating " + count + " objects");
            e.printStackTrace();
            System.exit(0);
        }
    }
}

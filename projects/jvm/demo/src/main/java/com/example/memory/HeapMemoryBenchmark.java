package com.example.memory;
import java.nio.ByteBuffer;
public class HeapMemoryBenchmark {
    public static void main(String[] args) {
        int bufferSize = 100 * 1024 * 1024; // 100MB
        long start = System.currentTimeMillis();

        ByteBuffer src = ByteBuffer.allocate(bufferSize); // 堆内存
        ByteBuffer dest = ByteBuffer.allocate(bufferSize);

        for (int i = 0; i < bufferSize; i++) {
            src.put((byte) 1);
        }

        src.flip();
        dest.put(src);

        long end = System.currentTimeMillis();
        System.out.println("Heap memory copy time: " + (end - start) + " ms");
    }
    
}

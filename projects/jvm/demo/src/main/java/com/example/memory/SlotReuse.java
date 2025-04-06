package com.example.memory;

public class SlotReuse {
    public static void main(String[] args) {
        {
            int a = 100;     // slot 1
            System.out.println(a);

        }
        {
            double b = 3.14; // slot 1 and 2（复用了a的槽位）
            System.out.println(b);
        }

    }
}

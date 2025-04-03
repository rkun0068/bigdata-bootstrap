## JVM
JVM 本质上是一个运行在计算机上的程序,用来运行Java字节码文件

<img src="https://upload.wikimedia.org/wikipedia/commons/d/dd/JvmSpec7.png" width = "500" height = "250">

## Java Memory Area
- [The JVM Run-Time Data Areas](https://www.baeldung.com/java-jvm-run-time-data-areas)
###  PC Register
- 每个线程会通过PC Register记录当前要执行的的字节码指令的地址。

## Memory Optimization
```
public class SimplePC {
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int sum = a + b;
        System.out.println(sum);
    }
}
```
反编译bytecode文件
```
Compiled from "SimplePC.java"
public class com.example.memory.SimplePC {
  public com.example.memory.SimplePC();
    Code:
       0: aload_0
       1: invokespecial #8                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: iconst_1  // 常量1入栈
       1: istore_1  // 存入局部变量a
       2: iconst_2
       3: istore_2
       4: iload_1   // 读取a
       5: iload_2
       6: iadd  // 执行a+b
       7: istore_3
       8: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;
      11: iload_3
      12: invokevirtual #22                 // Method java/io/PrintStream.println:(I)V
      15: return
}
```
- 在加载阶段，虚拟机将字节码文件中的指令读取到内存之后，会将原文件中的偏移量转换成内存地址。每一条字
节码指令都会拥有一个内存地址。
- 在代码执行过程中，程序计数器会记录下一行字节码指令的地址。执行完当前指令之后，虚拟机的执行引擎根据
程序计数器执行下一行指令。
- 程序计数器可以控制程序指令的进行，实现分支、跳转、异常等逻辑。
- 在多线程执行情况下，Java虚拟机需要通过程序计数器记录CPU切换前解释执行到那一句指令并继
续解释运行。



**程序计数器（PC）不会发生内存溢出（OutOfMemoryError, OOM）**

- **PC 只存储一个固定长度的内存地址**（指向当前线程执行的字节码指令）。  
- **不会动态分配额外内存**，所以 **不会超过 JVM 设定的内存上限**。  
- **每个线程都有独立的 PC**，它在 **线程切换时** 仅用于记录 **下一条要执行的指令地址**，并不会存储数据。  




### Leak
- [java-memory-leaks](https://www.baeldung.com/java-memory-leaks)


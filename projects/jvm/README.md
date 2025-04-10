## JVM
JVM 本质上是一个运行在计算机上的程序,用来运行Java字节码文件

<img src="https://upload.wikimedia.org/wikipedia/commons/d/dd/JvmSpec7.png" width = "500" height = "250">

## Java Memory Area
- [The JVM Run-Time Data Areas](https://www.baeldung.com/java-jvm-run-time-data-areas)
###  PC Register
- 每个线程会通过PC Register记录当前要执行的的字节码指令的地址。


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


### Stack


Java虚拟机栈（Java Virtual Machine Stack）采用栈的数据结构来管理方法调用中的基本数据，先
进后出（First In Last Out）,每一个方法的调用使用一个栈帧（Stack Frame）来保存。
- [Java/JVM Stacks and Stack Frames](https://alvinalexander.com/scala/fp-book/recursion-jvm-stacks-stack-frames/)
- [Java Virtual Machine Stacks](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.5.2)
### Stack Frame组成
- [Local Variables](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.6.1)
>局部变量表的作用是在运行过程中存放所有的局部变量
- [Operand Stacks](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.6.1)
> 操作数栈是栈帧中虚拟机在执行指令过程中用来存放临时数据的一块区域
- Frame Data
> 帧数据主要包含动态链接、方法出口、异常表的引用
#### Local Variables 
局部变量表的作用是在方法执行过程中存放所有的局部变量。编译成字节码文件时就可以确定局部变
量表的内容。
```
# Refer projects/jvm/demo/src/main/java/com/example/memory/SimplePC.java
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      16     0  args   [Ljava/lang/String;
            2      14     1     a   I
            4      12     2     b   I
            8       8     3   sum   I
```
- 栈帧中的局部变量表是一个数组，数组中每一个位置称之为槽(slot) ，long和double类型占用两个槽，其
他类型占用一个槽。

查看[ParamOrder.java](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/jvm/demo/src/main/java/com/example/memory/ParamOrder.java)的`add`方法的本地变量表
```
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      26     0     x   I
            0      26     1     y   D
            0      26     3     z   Ljava/lang/String;
            4      22     4 local   I
```
- 方法参数也会保存在局部变量表中，其顺序与方法中参数定义的顺序一致。

[SlotReuse.java](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/jvm/demo/src/main/java/com/example/memory/SlotReuse.java)
```
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      22     0  args   [Ljava/lang/String;
            3       7     1     a   I
           14       7     1     b   D
```
- 为了节省空间，局部变量表中的槽是可以复用的，一旦某个局部变量不再生效，当前槽就可以再次被使用。

##### Operand Stacks
>操作数栈是栈帧中虚拟机在执行指令过程中用来存放中间数据的一块区域。他是一种栈式的数据结构，如果一条指令将一个值压入操作数栈，则后面的指令可以弹出并使用该值。
>在编译期就可以确定操作数栈的最大深度，从而在执行时正确的分配内存大小。

```
# public class com.example.memory.SimplePC
stack=2, locals=4, args_size=1

 0: iconst_1             // 将整数常量1压入操作数栈
 1: istore_1             // 将1存入局部变量1（int a = 1）
 2: iconst_2             // 将整数常量2压入操作数栈
 3: istore_2             // 将2存入局部变量2（int b = 2）
 4: iload_1              // 加载局部变量1的值（a）
 5: iload_2              // 加载局部变量2的值（b）
 6: iadd                 // 执行加法操作：a + b
 7: istore_3             // 将结果存入局部变量3（int c = a + b）
 8: getstatic #16        // 获取 System.out 对象（PrintStream）
11: iload_3              // 加载局部变量3的值（c）
12: invokevirtual #22    // 调用 println(int) 方法，打印 c 的值
15: return               // 方法返回（void）


      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      16     0  args   [Ljava/lang/String;
            2      14     1     a   I
            4      12     2     b   I
            8       8     3   sum   I
```
#### Frame Data
- [Dynamic Linking](http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.6.3)

当前类的字节码指令引用了其他类的属性或者方法时，需要将符号引用（编号）转换成对应的运行时常量池
中的内存地址。动态链接就保存了编号到运行时常量池的内存地址的映射关系。
- [Normal Method Invocation Completion](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.6.4)

方法出口指的是方法在正确或者异常结束时，当前栈帧会被弹出，同时程序计数器应该指向上一个栈帧中的
下一条指令的地址。所以在当前栈帧中，需要存储此方法出口的地址。
- 异常表
[ExceptionTest](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/jvm/demo/src/main/java/com/example/memory/ExceptionTest.java)
异常表存放的是代码中异常的处理信息，包含了异常捕获的生效范围以及异常发生后跳转到的字节码指令位置。
```
      Exception table:
         from    to  target type
             0     5     8   Class java/lang/ArithmeticException
             0     5    28   Class java/lang/Exception
             0    17    48   any
            28    37    48   any
```
#### 栈内存溢出
- Java虚拟机栈如果栈帧过多，占用内存超过栈内存可以分配的最大大小就会出现内存溢出。
- Java虚拟机栈内存溢出时会出现StackOverflowError的错误。

[StackOverflowTest.java](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/jvm/demo/src/main/java/com/example/memory/StackOverflowTest.java)
> 使用递归让方法调用自身，但是不设置退出条件。定义调用次数的变量，每一次调用让变量加1。查看错误发生时总调用的次数。
```
Stack overflow after 22019 calls.
java.lang.StackOverflowError
```
- [Guide to JVM memory configuration options](https://bell-sw.com/blog/guide-to-jvm-memory-configuration-options/)
- [Java HotSpot VM Options](https://www.oracle.com/java/technologies/javase/vmoptions-jsp.html)
执行添加`-Xss4m`参数，设置栈大小为4MB
```
Stack overflow after 100888 calls.
java.lang.StackOverflowError 
```
#### [Native Method Stacks](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.5.6)
- Java虚拟机栈存储了Java方法调用时的栈帧，而本地方法栈存储的是native本地方法的栈帧。
- 在Hotspot虚拟机中，Java虚拟机栈和本地方法栈实现上使用了同一个栈空间。本地方法栈会在栈内存上生成一个栈帧，临时保存方法的参数同时方便出现异常时也把本地方法的栈信息打印出来。
### Heap
- 一般Java程序中堆内存是空间最大的一块内存区域。创建出来的对象都存在于堆上。
- 栈上的局部变量表中，可以存放堆上对象的引用。静态变量也可以存放堆对象的引用，通过静态变量就可以实
现对象在线程之间共享。

[HeapOverflowTest.java](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/jvm/demo/src/main/java/com/example/memory/HeapOverflowTest.java)
>通过new关键字不停创建对象，放入集合中，模拟堆内存的溢出，观察堆溢出之后的异常信息。堆内存大小是有上限的，当对象一直向堆中放入对象达到上限之后，就会抛出OutOfMemory错误。
```
🔥 OutOfMemoryError occurred after creating 23457 objects
java.lang.OutOfMemoryError: Java heap space
        at com.example.memory.HeapOverflowTest$BigObject.<init>(HeapOverflowTest.java:9)
        at com.example.memory.HeapOverflowTest.main(HeapOverflowTest.java:18)
```
[Difference in Used, Committed, and Max Heap Memory](https://www.baeldung.com/java-heap-used-committed-max)
- 堆空间有三个需要关注的值，used total max。
- used指的是当前已使用的堆内存，total是java虚拟机已经分配的可用堆内存，max是java虚拟机可以分配的最大堆内存。
- 随着堆中的对象增多，当total可以使用的内存即将不足时，java虚拟机会继续分配内存给堆。
- 如果堆内存不足，java虚拟机就会不断的分配内存，total值会变大。total最多只能与max相等
- 如果不设置任何的虚拟机参数，max默认是系统内存的1/4，total默认是系统内存的1/64。

设置堆大小
[Oracle官方文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html)
- 要修改堆的大小，可以使用虚拟机参数 –Xmx（max最大值）和-Xms (初始的total)。
- 单位：字节（默认，必须是 1024 的倍数）、k或者K(KB)、m或者M(MB)、g或者G(GB)
- 限制：Xmx必须大于 2 MB，Xms必须大于1MB
```
# 执行添加-Xms16m -Xmx32m
🔥 OutOfMemoryError occurred after creating 30 objects
java.lang.OutOfMemoryError: Java heap space
        at com.example.memory.HeapOverflowTest$BigObject.<init>(HeapOverflowTest.java:9)
        at com.example.memory.HeapOverflowTest.main(HeapOverflowTest.java:18)
```
- Java服务端程序开发时，建议将-Xmx和-Xms设置为相同的值，这样在程序启动之后可使用的总内存就是最大内存，而无
需向JVM再次申请，减少了申请并分配内存时间上的开销，同时也不会出现内存过剩之后堆收缩的情况。

### Leak
- [java-memory-leaks](https://www.baeldung.com/java-memory-leaks)


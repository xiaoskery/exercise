/* BTrace Script Template */

import static com.sun.btrace.BTraceUtils.jstack;
import static com.sun.btrace.BTraceUtils.println;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

@BTrace
public class TracingScript {
    /* put your code here */
    @OnMethod(clazz = "java.nio.ByteBuffer", method = "allocateDirect")
    public static void traceExecute() {
        println("who call ByteBuffer.allocateDirect :");
        jstack();// 打印线程栈
    }

    @OnMethod(clazz = "sun.misc.Unsafe", method = "allocateMemory")
    public static void traceExecute2() {
        println("who call Unsafe.allocateMemory :");
        jstack();// 打印线程栈
    }
}
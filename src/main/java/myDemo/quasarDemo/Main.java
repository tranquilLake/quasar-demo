package myDemo.quasarDemo;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 * 协程测试
 */
public class Main {

    /**
     * 方法入口
     */
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Program is running.");
        System.out.println("==========================================================");

        simpleFiber();// 简单的Fiber用法

        Thread.sleep(10000L);
        System.out.println("==========================================================");
    }

    /**
     * 简单的Fiber用法
     */
    private static void simpleFiber() {

        Fiber<Integer> fiber = new Fiber<Integer>() {
            @Override
            protected Integer run() throws SuspendExecution, InterruptedException {
                System.out.println("hello fiber");
                return super.run();
            }
        };

        fiber.start();
    }
}

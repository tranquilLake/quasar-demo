package myDemo.quasarDemo;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.util.concurrent.ExecutionException;

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

//        simpleFiber(); // 简单的Fiber用法
//        notJoinFiber(); // 不等待fiber执行完毕
//        joinFiber(); // 等待fiber执行完毕
//        initWithSuspendableRunable(); // 初始化，使用SuspendableRunnable

        Thread.sleep(3000L);
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

    /**
     * 不等待fiber执行完毕
     */
    private static void notJoinFiber() {
        Fiber<Integer> fiber = new Fiber<Integer>() {
            @Override
            protected Integer run() throws SuspendExecution, InterruptedException {
                System.out.println("print in fiber");
                return super.run();
            }
        };

        fiber.start();

        System.out.println("print out fiber");
    }

    /**
     * 等待fiber执行完毕
     */
    private static void joinFiber() {
        Fiber<Integer> fiber = new Fiber<Integer>() {
            @Override
            protected Integer run() throws SuspendExecution, InterruptedException {
                System.out.println("print in fiber");
                return super.run();
            }
        };

        fiber.start();

        try {
            fiber.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("print out fiber");
    }

    /**
     * 初始化，使用SuspendableRunnable
     */
    private static void initWithSuspendableRunable() {
        Fiber<Void> fiber1 = new Fiber<>(new SuspendableRunnable() {
            @Override
            public void run() throws SuspendExecution, InterruptedException {
                System.out.println("hello SuspendableRunnable");
            }
        });

        new Fiber<Void>((SuspendableRunnable) () -> System.out.println("hello SuspendableRunnable, again"));
    }
}

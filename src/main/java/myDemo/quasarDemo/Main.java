package myDemo.quasarDemo;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 协程测试
 */
public class Main {

    /**
     * 方法入口
     */
    public static void main(String[] args) throws Exception {

        System.out.println("Program is running.");
        System.out.println("==========================================================");


//        simpleFiber(); // 简单的Fiber用法
//        notJoinFiber(); // 不等待fiber执行完毕
//        joinFiber(); // 等待fiber执行完毕
//        initWithSuspendableRunable(); // 初始化，使用SuspendableRunnable
//        getFiberResultSync(); // 以同步的方式获取fiber的执行结果
        ThreadLocalInFiber(); // fiber 中的thread local

        Thread.sleep(3000L);
        System.out.println("==========================================================");
    }


    /**
     * 简单的Fiber用法
     */
    private static void simpleFiber() {

        Fiber<Integer> fiber = new Fiber<Integer>() {                                                                   // 创建一个fiber
            @Override
            protected Integer run() throws SuspendExecution, InterruptedException {
                System.out.println("hello fiber");                                                                      // 在fiber中打印一些文本
                return super.run();
            }
        };

        fiber.start();                                                                                                  // 启动一个fiber
    }


    /**
     * 不等待fiber执行完毕
     */
    private static void notJoinFiber() {
        Fiber<Integer> fiber = new Fiber<Integer>() {                                                                   // 创建一个fiber
            @Override
            protected Integer run() throws SuspendExecution, InterruptedException {
                System.out.println("print in fiber");
                return super.run();
            }
        };

        fiber.start();

        System.out.println("print out fiber");                                                                          // 在fiber启动之后打印一些文本
        // 文本先于fiber中打印的文本
    }

    /**
     * 等待fiber执行完毕
     */
    private static void joinFiber() {
        Fiber<Integer> fiber = new Fiber<Integer>() {                                                                   // 创建fiber，并且启动
            @Override
            protected Integer run() throws SuspendExecution, InterruptedException {
                System.out.println("print in fiber");
                return super.run();
            }
        };

        fiber.start();

        try {
            fiber.join();                                                                                               // 等待fiber执行完毕
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("print out fiber");
    }

    /**
     * 初始化，使用SuspendableRunnable
     */
    private static void initWithSuspendableRunable() {
        Fiber<Void> fiber1 = new Fiber<>(new SuspendableRunnable() {                                                    // 创建fiber对象，并且实现run方法
            @Override
            public void run() throws SuspendExecution, InterruptedException {
                System.out.println("hello SuspendableRunnable");
            }
        });

        Fiber<Void> fiber2 = new Fiber<>((SuspendableRunnable) () ->                                                    // 创建fiber对象，以一个SuspendableRunnable对象作为参数，
            System.out.println("hello SuspendableRunnable, again"));                                                    // 这种方法可以使用java8中的lambda表达式

        fiber1.start();
        fiber2.start();
    }

    /**
     * 以同步的方式获取fiber的执行结果
     */
    private static void getFiberResultSync() throws ExecutionException, InterruptedException {
        Fiber<String> fiber = new Fiber<String>(() -> "fiber result").start();                                          // 创建一个会返回"fiber result"的fiber
        String fiberResult = fiber.get();                                                                               // 等待执行完毕，并且获取返回值
        System.out.println(fiberResult);                                                                                // 打印
    }

    /**
     * fiber 中的thread local
     */
    private static void ThreadLocalInFiber() throws ExecutionException, InterruptedException {

        List<String> list = new ArrayList<>();
        list.add("there is a car");
        list.add("there is a house");
        list.add("there is a tree");
        list.add("there is a river");

        for (String s : list) {
            Fiber<Object> fiber = new Fiber<>(() -> {
                threadLocal.set(s);

                workOne();
                workTwo();

                String result = threadLocal.get();
                System.out.println(result);
            }).start();
        }

    }

    // 上面的demo中用到的thread local
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    // 将一个句子的首字母大写
    private static void workOne() {
        String sentence = threadLocal.get();
        if (sentence != null && sentence.length() > 0)
            sentence = sentence.substring(0, 1).toUpperCase() + sentence.substring(1);
        threadLocal.set(sentence);
    }

    // 在句子的最后添加标点符号
    private static void workTwo() {
        String sentence = threadLocal.get();
        if (sentence != null && sentence.length() > 0)
            sentence = sentence + ".";
        threadLocal.set(sentence);
    }

}

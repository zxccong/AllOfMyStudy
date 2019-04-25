package zxc.peason;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 9.线程池并发的应用
 * 学习java 5的Executors
 *
 * 线程是不会死的
 * 在线程空闲没有事情做的时候就关闭
 * threadPool.shutdown()
 *
 * 固定大小的线程池
 *ExecutorService threadPool = Executors.newFixedThreadPool(3);
 * 缓存的线程池
 *ExecutorService threadPool = Executors.newCachedThreadPool();
 * //单例线程的池
 * ExecutorService threadPool = Executors.newSingleThreadExecutor();
 *
 * 线程池启动定时器 ,Runnerable,时间，时间单位
 *
 * Executors.newScheduledThreadPool(3).schedule(new Runnable() {
 *             @Override
 *             public void run() {
 *                 System.out.println("booming");
 *             }
 *         },10, TimeUnit.SECONDS);
 *
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        //创建线程池,大小未3个
        //固定大小的线程池
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        //缓存大小的线程池
//        ExecutorService threadPool = Executors.newCachedThreadPool();
        //单例线程的池（线程池中只有一个线程）如果线程死了自动会重启
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        for (int i=1;i<=10;i++) {
            final int task=i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 10; i++) {
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + " loop of i = " + i + " for task  task = "+ task);
                    }
                }
            });

        }
//        threadPool.shutdown();
//        threadPool.shutdownNow();立刻关闭

        //单独炸一次
//        Executors.newScheduledThreadPool(3).schedule(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("booming");
//            }
//        },10, TimeUnit.SECONDS);
        //固定频率的
        Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("booming");
            }
        },10,10, TimeUnit.SECONDS);
    }
}

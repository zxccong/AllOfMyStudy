package zxc.peason;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 16.CpimtDownLatch
 *CountDownLatch cdOrder = new CountDownLatch(1);
 * 一个线程等待
 * cdOrder.await();
 * 另个线程减去初始化时候的值
 * cdOrder.countDown();
 * 当到达0时，等待的线程运行
 *
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch cdOrder = new CountDownLatch(1);
        CountDownLatch cdAnswer = new CountDownLatch(3);
        for (int i = 0 ;i >3;i++){
            Runnable runnable= new Runnable() {
                @Override
                public void run() {

                    try {

                        System.out.println("线程"+Thread.currentThread().getName()+
                                "正准备接收命令");
                        cdOrder.await();
                        System.out.println("线程"+Thread.currentThread().getName()+
                                "已经接受命令");
                        Thread.sleep((long)(Math.random()*10000));
                        System.out.println("线程"+Thread.currentThread().getName()+
                                "回应命令处理结果");
                        cdAnswer.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            };
            service.execute(runnable);
        }
        try {
            Thread.sleep((long)(Math.random()*10000));
            System.out.println("线程"+Thread.currentThread().getName()+
                    "即将发布命令");
            cdOrder.countDown();
            System.out.println("线程"+Thread.currentThread().getName()+
                    "已经发送命令，正在等待处理结果");
            cdAnswer.await();
            System.out.println("线程"+Thread.currentThread().getName()+
                    "已经收到所有响应结果");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

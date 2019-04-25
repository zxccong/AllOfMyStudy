package zxc.peason;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 14JAVA5中Semaphere同步工具
 *
 * 信号的可以在创建的时候定义是否公平
 *
 * 也可以单独定义一个信号灯作为互斥锁使用
 * 但是这个互斥锁别人可以释放
 * 不同于互斥锁只有自己才能释放
 *
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        final Semaphore sp= new Semaphore(3);
        for (int i = 0 ;i<10;i++) {
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    try {
                        sp.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程"+Thread.currentThread().getName()+
                            "进入，当前已有"+(3-sp.availablePermits())+"可用的信号");

                    try {
                        Thread.sleep((long)(Math.random()*10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sp.release();
                    System.out.println("线程"+Thread.currentThread().getName()
                                        +"已离开，当前已有"+(3-sp.availablePermits())+"可用的信号");

                }
            };

            service.execute(runnable);
        }
    }
}

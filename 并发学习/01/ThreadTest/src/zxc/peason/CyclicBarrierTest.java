package zxc.peason;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 15.CyclicBarrier同步工具类
 *
 * 多个线程同时到达才执行
 * CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
 * cyclicBarrier.await();
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        for (int i = 0 ;i<3;i++){
            Runnable runnable= new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long) Math.random()*10000);
                        System.out.println("线程"+Thread.currentThread().getName()+
                                "即将到达集合地点，当前已有"+(cyclicBarrier.getNumberWaiting()));


                            cyclicBarrier.await();


                        Thread.sleep((long) Math.random()*10000);
                        System.out.println("线程"+Thread.currentThread().getName()+
                                "即将到达集合地点，当前已有"+(cyclicBarrier.getNumberWaiting()));

                        cyclicBarrier.await();

                        Thread.sleep((long) Math.random()*10000);
                        System.out.println("线程"+Thread.currentThread().getName()+
                                "即将到达集合地点，当前已有"+(cyclicBarrier.getNumberWaiting()));

                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            service.execute(runnable);
        }

    }
}

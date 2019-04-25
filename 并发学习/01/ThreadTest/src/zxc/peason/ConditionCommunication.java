package zxc.peason;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 13.JAVA5阻塞同学Conditiont应用
 *
 * 在锁上建立Condition
 * Condition condition =lock.newCondition();
 *
 * 从Condition等待
 * condition.await();
 * 从Condition中通知
 * condition.signal();
 */
public class ConditionCommunication {

    public static void main(String[] args) {

        final Business business = new Business();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i<= 50 ; i++) {
                    business.sub(i);
                }
            }
        }).start();


        //main 方法本身就有一个线程，所以在main方法接着写
        for (int i = 1 ; i<= 50 ; i++) {
            business.main(i);

        }
    }

    /**
     * 要用到共同数据（包括同步锁）或共同算法
     * 的若干方法应该归在同一个类身上，
     * 这种设计正好体现了高类聚程序的健壮性
     */

    /**
     * 互斥的代码应该放在资源内部上的
     * 线程只是调用他们的方法
     * 调用他们的资源
     *
     * 好处在于以后线程调用就不用考虑线程同步的问题
     * 只需要调用线程调用既可
     */
    static class Business{

        Lock lock= new ReentrantLock();
        Condition condition =lock.newCondition();

        private boolean bShouldSub = true;

        //实现互斥1到10或者1-100
        public /*synchronized*/ void sub(int i){
            //while比if健壮性更加好，因为每次唤醒后都再次判断值是否正确
            lock.lock();
            try {
                while (!bShouldSub) {
                    try {
//                        this.wait();
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 0; j <= 10; j++) {
                    System.out.println("sub thread sequece of " + j + ",loop of " + i);
                }
                bShouldSub = false;
//                this.notify();
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public /*synchronized*/ void main(int i){
            lock.lock();
            try {
                while (bShouldSub) {
                    try {
//                        this.wait();
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                for (int j = 0; j <= 100; j++) {
                    System.out.println("main thread sequece of " + j+",loop of "+ i);
                }
                bShouldSub = true;
                condition.signal();
//                this.notify();
            } finally {
                lock.unlock();
            }
        }
    }
}



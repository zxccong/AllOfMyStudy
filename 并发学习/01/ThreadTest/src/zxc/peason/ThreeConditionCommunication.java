package zxc.peason;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 13.JAVA5阻塞同学Conditiont应用
 *
 * condition的应用
 * （ArrayBlockingQueue 类提供了这项功能，因此没有理由去实现这个示例类。）
 *
 */
public class ThreeConditionCommunication {

    public static void main(String[] args) {

        final Business business = new Business();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i<= 50 ; i++) {
                    business.sub1(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i<= 50 ; i++) {
                    business.sub2(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ; i<= 50 ; i++) {
                    business.sub3(i);
                }
            }
        }).start();


//        //main 方法本身就有一个线程，所以在main方法接着写
//        for (int i = 1 ; i<= 50 ; i++) {
//            business.main(i);
//
//        }
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
    class Business{

        Lock lock= new ReentrantLock();
        Condition condition1 =lock.newCondition();
        Condition condition2 =lock.newCondition();
        Condition condition3 =lock.newCondition();

        private int bShouldSub = 1;

    //实现互斥1到10或者1-100
        public /*synchronized*/ void sub1(int i){
            //while比if健壮性更加好，因为每次唤醒后都再次判断值是否正确
            lock.lock();
            try {
                while (bShouldSub!=1) {
                    try {
//                        this.wait();
                        condition1.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 0; j < 1; j++) {
                    System.out.println("sub1 thread sequece of " + j + ",loop of " + i);
                }
                bShouldSub = 2;
//                this.notify();
                condition2.signal();
            } finally {
                lock.unlock();
            }
        }

        public /*synchronized*/ void sub2(int i){
            lock.lock();
            try {
                while (bShouldSub!=2) {
                    try {
//                        this.wait();
                        condition2.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                for (int j = 0; j < 1; j++) {
                    System.out.println("sub2 thread sequece of " + j+",loop of "+ i);
                }
                bShouldSub = 3;
                condition3.signal();
//                this.notify();
            } finally {
                lock.unlock();
            }
        }

    public /*synchronized*/ void sub3(int i){
        lock.lock();
        try {
            while (bShouldSub !=3) {
                try {
//                        this.wait();
                    condition3.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            for (int j = 0; j < 1; j++) {
                System.out.println("sub3 thread sequece of " + j+",loop of "+ i);
            }
            bShouldSub = 1;
            condition1.signal();
//                this.notify();
        } finally {
            lock.unlock();
        }
    }
    }

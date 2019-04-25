package zxc.peason;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 5.线程范围内共享变量的概念和作用
 *
 * 目的是要在不同的线程同时去公共资源static
 * 取得的数据不一样
 * 未处理之前输出是这样的
 * Thread-0has put data of :621050701
 * Thread-1has put data of :-1027403751
 * A from Thread-1get data :-1027403751
 * A from Thread-0get data :-1027403751
 * A from Thread-1get data :-1027403751
 * A from Thread-0get data :-1027403751
 *
 * 线程范围内的数据共享
 */
public class ThreadScopeShareData {

    private static int data =0 ;

    private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();

    public static void main(String[] args) {

        for (int i=0 ;i<2;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data=new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+"has put data of :"+data);
                    threadData.put(Thread.currentThread(),data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }


    }

    static class A {
        public void get(){
            int data = threadData.get(Thread.currentThread());
            System.out.println("A from "+Thread.currentThread().getName()+"get data :"+data);
        }
    }

    static class B {
        public void get(){
            int data = threadData.get(Thread.currentThread());
            System.out.println("B from "+Thread.currentThread().getName()+"get data :"+data);
        }
    }
}

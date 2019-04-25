package zxc.peason;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 11.java5线程锁技术Locks类的学习
 * 将传统的synchronized的替换为Lock更加体现面向对象的思想
 * Lock lock= new ReentrantLock();
 * lock.lock()
 *
 * try - finally -
 *
 * lock.unlock();
 *
 */
public class LockTest {

    public static void main(String[] args) {
        new LockTest().init();
    }

    private void init(){
        final   Outputer outputer = new Outputer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    outputer.output("zhangxiaocong");

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    outputer.output("chendianqu");

                }
            }
        }).start();
    }

    static class Outputer{
        Lock lock= new ReentrantLock();
        public  void output(String name){
            int len = name.length();
                lock.lock();

            try {
                for (int i = 0;i< len;i++){
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            } finally {
                lock.unlock();
        }


        }
        //synchronized (this)
        public synchronized  void output2(String name){
            int len = name.length();

                for (int i = 0;i< len;i++){
                    System.out.print(name.charAt(i));
                }
                System.out.println();

        }

        //synchronized(Outputer.calss)
        public static synchronized  void output3(String name){
            int len = name.length();

            for (int i = 0;i< len;i++){
                System.out.print(name.charAt(i));
            }
            System.out.println();

        }
    }
}

package zxc.peason;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 12java5读写锁技术
 * 读与读不互斥
 * 读与写互斥
 * 写与写互斥
 *
 * private ReadWriteLock rwl =new ReentrantReadWriteLock();
 *
 * //读锁
 * rwl.readLock().lock();
 *
 * //写锁
 * rwl.writeLock().lock();
 *
 *
 *
 */
public class ReadWriteTest {

    public static void main(String[] args) {
        final  Queue3 q3 = new Queue3();
        for (int i =0 ;i<3;i++){
            new Thread(){
                @Override
                public void run() {
                    q3.get();
                }
            }.start();

            new Thread(){
                @Override
                public void run() {
                    q3.put(new Random().nextInt(10000));
                }
            }.start();
        }

    }
}

class Queue3{
    private Object data= null;
    private ReadWriteLock rwl =new ReentrantReadWriteLock();
    public void get(){
        rwl.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+" be ready to read");

            try {
                Thread.sleep((long)(Math.random()*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" have read data : "+data);
        } finally {
            rwl.readLock().unlock();
        }


    }

    public void put(Object data){
        rwl.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+" be ready to write data!");
            try {
                Thread.sleep((long)(Math.random()*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.data=data;
            System.out.println(Thread.currentThread().getName()+" have write data ："+ data);
        } finally {
            rwl.writeLock().unlock();
        }

    }
}

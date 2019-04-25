package zxc.peason;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 18.阻塞队列
 *         exception（抛出异常）    return null   blocks（阻塞）
 * insert       add()                 offer()        put()
 * remove      remove()               poll()         take()
 */
public class BlockingQueueTest {
    public static void main(String[] args) {
        BlockingQueue queue= new ArrayBlockingQueue(3);
        for (int i=0;i<2;i++){
            new Thread(){
                @Override
                public void run() {
                    while(true){
                        try {
                            Thread.sleep((long)Math.random()*1000);
                            System.out.println(Thread.currentThread().getName()+"准备放数据");
                            queue.put(1);
                            System.out.println(Thread.currentThread().getName()+"成功放数据，目前队列有"+queue.size()+"个数据");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(100);
                        System.out.println(Thread.currentThread().getName()+"准备取数据");
                        queue.take();
                        System.out.println(Thread.currentThread().getName()+"成功取数据，目前队列有"+queue.size()+"个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}

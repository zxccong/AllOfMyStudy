package zxc.peason.interview;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 空中网面试题2
 *
 * 现成程序中的test类中的代码不断地产生数据，然后交给TestDo.doSome方法处理,就好像生产者消费者
 * 在不断的产生数据，消费者在不断的消费数据，请将程序改造有10个线程的消费生成者产生的数据，这些消费者都调用
 * TesetDo.doSome()方法去进行处理，故每一个消费者都需要一秒才能处理完，程序应保证这些消费者线程依次有序地消费数据
 * ，只有上一个消费者消费完成后，下一个消费者才能消费数据，下一个消费者是谁都可以，但要保证这些消费者拿到的数据是有序的
 *
 * SynchronousQueue
 * 帮助文档：一种阻塞队列，其中每个插入操作必须等待另一个线程的对应移除操作、
 * 意思是没有人读取就put不进去,一旦有人读取就立即插入到队列中
 */
public class InterviewTwo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        SynchronousQueue<String> queue = new SynchronousQueue<>();

        for (int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        String input = queue.take();
                        String output = TestDo.doSome(input);
                        System.out.println(Thread.currentThread().getName()+":"+output);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        System.out.println("bengin:"+(System.currentTimeMillis()/1000));
        for (int i= 0; i<10; i++){ //不能改
            String input=i+"";      //不能改

            try {
                queue.put(input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
//模拟消费者
//不能改
class TestDo{
    public static String doSome(String input){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String output = input +"："+(System.currentTimeMillis()/1000);
        return output;
    }
}

package zxc.peason.interview;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 空中网线程面试题1
 */
public class interviewone {
    public static void main(String[] args) {
        BlockingQueue<String> queue= new ArrayBlockingQueue(16);
        for (int i=0;i<4;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){
                            String log =  queue.take();
                            interviewone.parseLog(log);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        System.out.println("begin:"+(System.currentTimeMillis()/1000));
        /*模拟处理16行数据，下面的代码产生了16个日志对象，当前代码
        * 修改程序代码，开启四个线程让这16个对象在4秒内打完
        * */
        for (int i=0; i<16;i++){    //不能动
            final  String log= "" +(i+1); //不能动
            {
                try {
                    queue.put(log);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                interviewone.parseLog(log);
            }
        }
    }

    public static void parseLog(String log){
        System.out.println(log+":"+System.currentTimeMillis()/1000);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

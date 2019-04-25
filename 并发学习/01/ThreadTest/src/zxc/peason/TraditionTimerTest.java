package zxc.peason;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 2.传统的定时器
 */
public class TraditionTimerTest {
    static int count = 0;
    public static void main(String[] args) {

        //第一个参数是第一个触发的时间，第二个参数是每隔多时间再炸一次
//        Timer timer = new Timer();
//        //定时器的调度器
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("bombing!");
//            }
//        },10000,3000);

        class MyTimerTask extends TimerTask{

            @Override
            public void run() {
                 count = (count + 1) % 2;
                System.out.println("bombing");
                new Timer().schedule(/*new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("bombing");
                    }
                }*/new MyTimerTask(),2000+2000*count);
            }
        }


        //需求2秒炸一次4秒炸一次
        new Timer().schedule(new MyTimerTask()
           ,2000);

        //注释掉了也会运行定时任务
        while (true){
            System.out.println(new Date().getSeconds());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //插件quartz周一、周三

    }
}

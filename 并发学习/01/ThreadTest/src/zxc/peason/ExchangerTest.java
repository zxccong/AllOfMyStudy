package zxc.peason;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 17 Exchanger同步工具类
 * Exchanger exchanger = new Exchanger();
 * String data2= (String) exchanger.exchange(data1);
 * 交换等待对方到达交换
 */
public class ExchangerTest {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        Exchanger exchanger = new Exchanger();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data1="zzz";
                    System.out.println("线程"+Thread.currentThread().getName()+"准备交换数据"+data1);
                    Thread.sleep((long)Math.random()*10000);
                    String data2= (String) exchanger.exchange(data1);
                    System.out.println("线程"+Thread.currentThread().getName()+"交换回来的数据未"+data2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data1="xxx";
                    System.out.println("线程"+Thread.currentThread().getName()+"准备交换数据"+data1);
                    Thread.sleep((long)Math.random()*10000);
                    String data2= (String) exchanger.exchange(data1);
                    System.out.println("线程"+Thread.currentThread().getName()+"交换回来的数据未"+data2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

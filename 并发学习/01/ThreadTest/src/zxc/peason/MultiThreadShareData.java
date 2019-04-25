package zxc.peason;

/**
 * 7.面试题
 * 设计4个线程，其中两个线程每次对j增加1
 * ，另外两个线程对j每次减1
 * 多个线程共享数据
 *
 * 主要有三种实现方法
 *
 * 先引入
 * 多个线程秒杀商品的例子
 * 建立同一个runnerable对象
 * 数量存在Runnerable类里面
 * 不断调用减小的run（）方法
 *
 *
 * 第一种实现方式建立同一个类
 * 类中有data值
 * 一个方法加，一个方法减两个方法都要加上synchronized实现线程的同步
 * 实例化一个该类的final修饰的对象
 * 用一个线程去调用减方法
 * 一个线程去调用加方法
 *
 * 第二种实现方式
 * 建立一个共享数据的类要么用final修饰的局部变量或者是static的全局变量
 * 建立一个Runnerable类里面的构造方法需要这么个共享值
 * 一个Runnerable的run（）方法加一个run()方法减
 * 调用2个线程去分别调用这2个Runnerable类
 * 实现
 *
 * 第三种方法就是这2种方法的结合
 * 在建立2个Runnerable的类调用主类中的方法的synchronized的加减方法
 * 建立线程实现2个Runnerable类
 *
 *
 * 总结，互斥的加减方法封装到方法中用synchronized修饰
 * 在单独用线程调用
 *
 *
 */
public class MultiThreadShareData {
    public static void main(String[] args) {

        new Thread().start();
        new Thread().start();


    }
}

class ShareData1 /*implements Runnable*/ {
    private int j=0;
    public synchronized void increment(){
        j++;
    }

    public synchronized void decrement(){
        j--;
    }

//    private int count =100;

//    @Override
//    public void run() {
//        while (true){
//            count--;
//        }
//    }
}

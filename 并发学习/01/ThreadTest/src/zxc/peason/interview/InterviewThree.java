package zxc.peason.interview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 空中网面试题3
 * 现在程序同时启动4个线程去调用TestDo.doSome(key,value)方法，由于TestDo.doSome(key,value)
 * 方法内的代码是暂停一秒，然后再输出以秒为单位的当时时间值，所以会打印出4个相同的时间值，
 * 4：4：1258199615
 * 1：1：1258199615
 * 3：3：1258199615
 * 1：2：1258199615
 * 请修改代码，如果几个线程调用TestDo.doSome(key,value)方法时，传递进去的key相等(equals比较为true）
 * 则这几个线程应互斥排队输出结果，即当有两个线程key都为1时，它们中的一个要比另外其他线程晚一秒输出结果，
 *      4：4：1258199615
 *      1：1：1258199615
 *      3：3：1258199615
 *      1：2：1258199615
 */
//不能改动此test类
public class InterviewThree extends Thread {
    private TesttDo testtDo;
    private String key;
    private String value;
    public InterviewThree(String key,String key2,String value){
        this.testtDo=TesttDo.getInstance();
        this.key=key+key2;
        this.value = value;
    }

    public static void main(String[] args) {
        InterviewThree a = new InterviewThree("1","","1");
        InterviewThree b = new InterviewThree("1","","2");
        InterviewThree c = new InterviewThree("3","","3");
        InterviewThree d = new InterviewThree("4","","4");
        System.out.println("begin:"+(System.currentTimeMillis()/1000));
        a.start();
        b.start();
        c.start();
        d.start();


    }

    public void run(){
        testtDo.doSome(key,value);
    }
}
class TesttDo{
    private TesttDo(){}
    private static TesttDo instance = new TesttDo();
    public static TesttDo getInstance(){
        return instance;
    }

//    private ArrayList keys = new ArrayList();
    private CopyOnWriteArrayList keys = new CopyOnWriteArrayList();
    public void doSome(Object key , String value){


        Object o  =key;

        if (!keys.contains(key)){
            keys.add(o);
        }else {

            for (Iterator iterator = keys.iterator();iterator.hasNext();){
                Object next = iterator.next();
                if (next.equals(key)){
                    o=next;
                }
            }
        }

        synchronized (o)
        //大括号以内是需要局部同步代码，不能改动！
        {
            try {
                Thread.sleep(1000);
                System.out.println(key+":"+value+":"+System.currentTimeMillis()/1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

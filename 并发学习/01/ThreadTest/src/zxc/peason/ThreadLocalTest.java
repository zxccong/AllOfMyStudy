package zxc.peason;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 6.ThreadLocalTest
 * 对ThreadLocalTest的学习
 * 线程范围内共享的变量ThreadLocal对象
 * 多个对象需要多个ThreadLocal
 * 或者直接打包成一个实体类
 *
 * 这里是一种比较傻瓜的方式多个值打包成实体类
 *
 * 内部实现相当与 Map<Thread,Intager> threadlocal = HashMap<Thread,Intager>()
 * 相当于放了一个当前线程对应的数据Map的结构
 */
public class ThreadLocalTest {

    static ThreadLocal<MyThreadScopeData1> x=new ThreadLocal<MyThreadScopeData1>();

    public static void main(String[] args) {

        for (int i=0 ;i<2;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data=new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+"has put data of :"+data);
                    MyThreadScopeData1 entity = new MyThreadScopeData1();
                    entity.setAge(data);
                    entity.setName("name"+data);
                    x.set(entity);
                    new A().get();
                    new B().get();
                }
            }).start();
        }


    }

    static class A {
        public void get(){
            MyThreadScopeData1 entity = x.get();
            System.out.println("A from "+Thread.currentThread().getName()+"get data :"+entity);
        }
    }

    static class B {
        public void get(){
            MyThreadScopeData1 entity = x.get();
            System.out.println("B from "+Thread.currentThread().getName()+"get data :"+entity);
        }
    }
}
class MyThreadScopeData1{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "MyThreadScopeData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

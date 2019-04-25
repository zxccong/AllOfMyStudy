package zxc.peason;

import java.util.Random;

/**
 * 6.ThreadLocalTest
 * 对ThreadLocalTest的学习
 * 线程范围内共享的变量ThreadLocal对象
 * 多个对象需要多个ThreadLocal
 * 或者直接打包成一个实体类
 *
 */
public class ThreadLocalTest2 {

//    static ThreadLocal<MyThreadScopeData> x=new ThreadLocal<MyThreadScopeData>();

    public static void main(String[] args) {

        for (int i=0 ;i<2;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data=new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+"has put data of :"+data);
//                    MyThreadScopeData entity = new MyThreadScopeData();
//                    entity.setAge(data);
//                    entity.setName("name"+data);
//                    x.set(entity);

                    MyThreadScopeData.getInstance().setName("name"+data);
                    MyThreadScopeData.getInstance().setAge(data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }


    }

    static class A {
        public void get(){
//            MyThreadScopeData entity = x.get();
//            System.out.println("A from "+Thread.currentThread().getName()+"get data :"+entity);

            MyThreadScopeData instance = MyThreadScopeData.getInstance();
            System.out.println("A from "+Thread.currentThread().getName()+"get data"+instance);
        }

    }

    static class B {
        public void get(){
            MyThreadScopeData instance = MyThreadScopeData.getInstance();
            System.out.println("A from "+Thread.currentThread().getName()+"get data"+instance);
        }
    }
}

//将这个实体类单例化
class MyThreadScopeData{
    private MyThreadScopeData(){

    }

    public static /*synchronized*/ MyThreadScopeData getInstance(){
        MyThreadScopeData instance =map.get();
        if (instance==null){
            instance = new MyThreadScopeData();
            map.set(instance);
        }
        return instance;

    }

//    private static MyThreadScopeData instance =null;

    private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();


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

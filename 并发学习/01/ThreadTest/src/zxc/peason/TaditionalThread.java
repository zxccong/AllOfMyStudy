package zxc.peason;

/**
 * 传统的线程
 */
public class TaditionalThread  {

    public static void main(String[] args) {

        //1.线程编写方式1
       Thread thread = new Thread(){
            @Override
            public void run() {
                while (true){
                    System.out.println("1:"+Thread.currentThread().getName());
                    System.out.println("2:"+this.getName());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
//        thread.start();

        //2.线程编写方法2
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    System.out.println("1:" + Thread.currentThread().getName());
                    //这里不能使用this因为这里是不是一个线程而是一个Runnable()
//                System.out.println("2:"+this.getName());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        thread2.start();

        //第二种更加体现面向对象编程



        //问题提出：同时声明会Runnable和子类的run()方法到底会执行哪一个
        //答案：Runnable的run()方法相当于父类run方法
        //      thread的run相当于子类的run 子类覆盖了父类的run方法
        //      所以说是运行子类的run 方法
        new Thread(
                new Runnable() {
                    @Override
                    public void run () {
                        System.out.println("Runnable:" + Thread.currentThread().getName());
                        //这里不能使用this因为这里是不是一个线程而是一个Runnable()
//                System.out.println("2:"+this.getName());

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ){
            @Override
            public void run() {
                while (true) {
                    System.out.println("thread:" + Thread.currentThread().getName());
                    //这里不能使用this因为这里是不是一个线程而是一个Runnable()
//                System.out.println("2:"+this.getName());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        //多线程会提高运行效率吗？
        //不会，有可能变得更慢；因为CPU资源总和是一样的

        //多线程下载会变快
        //实际上是占用的服务器的带宽







    }
}

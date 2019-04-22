package zxc.peason;

/**
 * 传统的线程同步
 */
public class TraditionThreadSynchronized {

    public static void main(String[] args) {
        new TraditionThreadSynchronized().init();
    }

    private void init(){
        final   Outputer outputer = new Outputer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    outputer.output("zhangxiaocong");

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    outputer.output3("chendianqu");

                }
            }
        }).start();
    }

    static class Outputer{
        public  void output(String name){
            int len = name.length();
            synchronized (Outputer.class) {
                for (int i = 0;i< len;i++){
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }
        }
        //synchronized (this)
        public synchronized  void output2(String name){
            int len = name.length();

                for (int i = 0;i< len;i++){
                    System.out.print(name.charAt(i));
                }
                System.out.println();

        }

        //synchronized(Outputer.calss)
        public static synchronized  void output3(String name){
            int len = name.length();

            for (int i = 0;i< len;i++){
                System.out.print(name.charAt(i));
            }
            System.out.println();

        }
    }
}

package zxc.peason;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 10CallableAndFuture的学习
 *
 * Future<String> submit = threadPool.submit(new Callable<String>() {
 *             @Override
 *             public String call() throws Exception {
 *
 *                 try {
 *                     Thread.sleep(20000);
 *                 } catch (InterruptedException e) {
 *                     e.printStackTrace();
 *                 }
 *                 return "hello";
 *             }
 *         });
 *
 * 先通过线程池提交一个Callable对象
 * 返回Future对象的get()获得结果
 * try {
 *             String s = submit.get();
 * //            String s = submit.get(1,TimeUnit.SECONDS);
 *             System.out.println("拿到结果"+s);
 *         } catch (InterruptedException e) {
 *             e.printStackTrace();
 *         } catch (Exception e) {
 *             e.printStackTrace();
 *         }
 *
 *
 *
 *
 */
public class CallableAndFuture {

    public static void main(String[] args) {
//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
//        Future<String> submit = threadPool.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return "hello";
//            }
//        });
//        System.out.println("等待结果");
//        try {
//            String s = submit.get();
////            String s = submit.get(1,TimeUnit.SECONDS);
//            System.out.println("拿到结果"+s);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ExecutorService threadPool1 = Executors.newFixedThreadPool(3);
        CompletionService completionService=new ExecutorCompletionService(threadPool1);

        for (int i = 0 ;i<=10;i++) {
            final int seq= i ;
            completionService.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    Thread.sleep(new Random().nextInt(5000));
                    return seq;
                }
            });
        }

        for (int i=0 ;i<10;i++){
            try {
                System.out.println("获得结果"+completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }





}

package zxc.peason;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 12.面试题
 * 建立一个缓存系统
 *
 * 建立缓存意思是建立一个Map存储一个键值对的形式
 * 同步直接再synchronized
 *
 * 最好是用读写锁
 *
 */
public class CacheDemo {

    private Map cache = new HashMap<String,Object>();

    public static void main(String[] args) {

    }
    ReadWriteLock bwl=new ReentrantReadWriteLock();
    public /*synchronized*/ Object getDate(String key){

        //添加一个读锁
        bwl.readLock().lock();
        Object value=null;
        try {
            if (value ==null){
                value= cache.get(key);
                //如果没有数据就释放掉读锁改为写锁
                bwl.readLock().unlock();
                bwl.writeLock().lock();
                try {
                    if (value==null) {
                        value = "aaa";
                    }
                } finally {
                    //设置数据完毕后再通过释放读锁
                    bwl.writeLock().unlock();
                }
                bwl.readLock().lock();

            }
        } finally {
            bwl.readLock().unlock();
        }

        return value;
    }
}

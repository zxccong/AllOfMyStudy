package zxc.peason;

/**
 * 8.学习java.util.concurrent.atomic类
 * 学习
 *
 * AtomicBoolean 原子增 ,原子减
 * AtomicInteger
 * AtomicIntegerArray
 * AtomicIntegerFieldUpdater  对一个类对象的某个值进行原子增加、减小
 * 先通过方法
 * <U> AtomicLongFieldUpdater<U>
 *  newUpdater(Class<U> tclass, String fieldName)
 *  为对象创建并返回一个具有给定字段的更新器。
 *  初始化、绑定字段
 *
 *   long addAndGet(T obj,long delta)
 *            以原子方式将给定值添加到此更新器管理的给定对象的字段的当前值
 *   而后对具体的实例化对象进行加减操作
 *
 *
 *
 */
public class StudyAtomClass {
}

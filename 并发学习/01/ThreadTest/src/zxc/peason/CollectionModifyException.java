package zxc.peason;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 19.同步集合类
 *
 * Collections.synchronizedMap()
 * 旧方法
 * 可以使用synchronizedMap方法转换普通的Map转为线程安全的集合类
 *
 *HashSet就是用了HashMap的key一列
 * public HashSet() {
 *         map = new HashMap<>();
 *     }
 *
 *
 * Collection users = new ArrayList();
 * 这个List不支持读的时候修改的
 * 异常：
 * 在迭代的过程中删除元素（删除李四不会出问题）
 * while(iterator.hasNext()){
 *             User user = (User) iterator.next();
 *             if ("张三".equals(user.getName())){
 *                 users.remove(user);
 *             }else {
 *                 System.out.println(user);
 *             }
 *         }
 *  异常解析：
 *  迭代的过程“李四”删掉大小hasNext判断后就没有数据了所以跳出循环导致next()方法的报错不会出现
 *  迭代过程“王五”运行到王五删除掉hasNext（）永远为真,到next()方法的时候报错
 *
 *
 * 修改为CopyOnWriteArrayList()可以在迭代的时候修改
 *Collection users = new CopyOnWriteArrayList();
 *
 *
 */
public class CollectionModifyException {
    public static void main(String[] args) {

        Collection users = new CopyOnWriteArrayList();
        users.add(new User("张三",28) );
        users.add(new User("李四",29) );
        users.add(new User("王五",30) );
        Iterator iterator = users.iterator();
        while(iterator.hasNext()){
            User user = (User) iterator.next();
            if ("张三 ".equals(user.getName())){
                users.remove(user);
            }else {
                System.out.println(user);
            }
        }

    }
}
class User{
    private String name;
    private int age ;

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

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

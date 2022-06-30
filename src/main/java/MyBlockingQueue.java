import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Lock 实现阻塞队列：先进先出、入队出队线程安全
 * </p>
 *
 * @author wenjun
 * @since 2022-06-30
 */
public class MyBlockingQueue<E> {

    /**
     * 阻塞队列最大容量
     */
    int size;

    /**
     * 队列底层实现
     */
    LinkedList<E> list = new LinkedList<>();

    ReentrantLock lock = new ReentrantLock();

    /**
     * 队列满时的等待条件
     */
    Condition notFull = lock.newCondition();

    /**
     * 队列空时的等待条件
     */
    Condition notEmpty = lock.newCondition();

    public MyBlockingQueue(int size) {
        this.size = size;
    }

    public void enqueue(E e) throws InterruptedException {
        lock.lock();
        try {
            // 队列已满，在 notFull 条件上等待
            while (list.size() == size) {
                notFull.await();
            }
            // 入队：加入链表末尾
            list.add(e);
            System.out.println("入队：" + e);
            // 通知在 notEmpty 条件上等待的线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        E e;
        lock.lock();
        try {
            // 队列为空，在 notEmpty 条件上等待
            while (list.size() == 0) {
                notEmpty.await();
            }
            // 出队：移除链表首元素
            e = list.removeFirst();
            System.out.println("出队：" + e);
            // 通知在 notFull 条件上等待的线程
            notFull.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(2);
        for (int i = 0; i < 10; i++) {
            int data = i;
            new Thread(() -> {
                try {
                    queue.enqueue(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    queue.dequeue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Lock Condition 测试 等待通知机制
 * </p>
 *
 * @author wenjun
 * @since 2022-06-30
 */
public class ConditionTest {

    static ReentrantLock lock = new ReentrantLock();

    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        lock.lock();
        new Thread(new SignalThread()).start();
        try {
            System.out.println("主线程等待通知");
            condition.await();
        } finally {
            lock.unlock();
        }
        System.out.println("主线程恢复运行");
    }

    static class SignalThread implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                condition.signal();
                System.out.println("子线程通知");
            } finally {
                lock.unlock();
            }
        }
    }
}

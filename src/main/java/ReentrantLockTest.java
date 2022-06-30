import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Lock 测试 可重入锁
 * </p>
 *
 * @author wenjun
 * @since 2022-06-30
 */
public class ReentrantLockTest {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        for (int i = 1; i <= 3; i++) {
            lock.lock();
            try {
            } finally {
                lock.unlock();
            }
        }
    }
}

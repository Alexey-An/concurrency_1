import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBUpdater implements Runnable {

    private final int isolation;

    public final AtomicLong myLockTime = new AtomicLong();
    public final AtomicLong totalTime = new AtomicLong();

    public final AtomicInteger cycleCounter = new AtomicInteger(0);

    private final Lock myLock = new ReentrantLock();

    public DBUpdater(int isolation) {
        this.isolation = isolation;
    }

    @Override
    public void run() {
        this.run2();
    }

    public void run1() {

        AppDataSource dataSource = AppDataSource.getDataSourceInstance();
        try ( Connection connection = dataSource.getNewConnection() ) {
            connection.setTransactionIsolation(isolation);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            System.out.println(Thread.currentThread().getName());
            for (int j = 0; j < 1000; j++) {
                long t0 = System.nanoTime();
                try {
                    myLock.lock();
                    long t1 = System.nanoTime();
                    myLockTime.addAndGet(t1 - t0);
                    ResultSet resultSet = statement.executeQuery("SELECT id, value FROM con");
                    resultSet.next();
                    int currentValue = resultSet.getInt("value");
                    resultSet.updateInt("value", currentValue + 1);
                    resultSet.updateRow();
                    connection.commit();
                    resultSet.close();
                    cycleCounter.getAndIncrement();
                } finally {
//                    long t2 = System.nanoTime();
                    myLock.unlock();
                    long t3 = System.nanoTime();
                    totalTime.addAndGet(t3  - t0);
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void run2() {

        AppDataSource dataSource = AppDataSource.getDataSourceInstance();
        try ( Connection connection = dataSource.getNewConnection() ) {
            connection.setTransactionIsolation(isolation);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            System.out.println(Thread.currentThread().getName());
            for (int j = 0; j < 1000; j++) {
                ResultSet resultSet = statement.executeQuery("SELECT id, value FROM con FOR UPDATE");
                resultSet.next();
                int currentValue = resultSet.getInt("value");
                resultSet.updateInt("value", currentValue + 1);
                resultSet.updateRow();
                connection.commit();
                resultSet.close();
                cycleCounter.getAndIncrement();
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

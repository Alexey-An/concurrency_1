import java.sql.Connection;

public class Application {

    public static void main(String[] args) {

        DBUpdater updater = new DBUpdater(Connection.TRANSACTION_READ_COMMITTED);
        DBUpdater2 updater2 = new DBUpdater2(Connection.TRANSACTION_READ_COMMITTED);
        Thread thread1 = new Thread(updater2);
        Thread thread2 = new Thread(updater);
        Thread thread3 = new Thread(updater);
        Thread thread4 = new Thread(updater);
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        thread2.start();
        thread3.start();
        thread4.start();
        try {
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        } finally {

            System.out.println("Job is done! " + updater.cycleCounter);
            System.out.println("Lock time: " + updater.myLockTime);
            System.out.println("Total time: " + updater.totalTime);

        }

    }
}

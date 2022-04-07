import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUpdater2 implements Runnable {

    private final int isolation;

    public DBUpdater2(int isolation) {
        this.isolation = isolation;
    }

    @Override
    public void run() {

        AppDataSource dataSource = AppDataSource.getDataSourceInstance();
        try ( Connection connection = dataSource.getNewConnection() ) {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            connection.setTransactionIsolation(isolation);
            System.out.println(Thread.currentThread().getName());
            boolean resultSet = statement.execute("UPDATE con SET value = 0");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


import org.postgresql.ds.common.BaseDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class AppDataSource extends BaseDataSource {

    private static AppDataSource instance;

    private static final String USER = "postgres";
    private static final String PSWD = "ki84";
    private static final String DBName = "mydb";
    private static final int [] PORTS = new int[]{5432};

    private static final String DESCRIPTION = "BaseDataSource for PostgreSQL database in a concurrent access app";

    @Override
    public String getDescription() {
        return this.DESCRIPTION;
    }

    public Connection getNewConnection() {
        try {
            return super.getConnection(USER, PSWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable to establish DB connection!!!");
    }

    public static AppDataSource getDataSourceInstance() {
        if (instance == null) {
            AppDataSource dataSource = new AppDataSource();
            dataSource.setPortNumbers(PORTS);
            dataSource.setDatabaseName(DBName);
            dataSource.setUser(USER);
            dataSource.setPassword(PSWD);
            return  dataSource;
        }
        return instance;
    }

    private AppDataSource() {
    }
}

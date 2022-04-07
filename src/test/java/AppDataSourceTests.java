import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AppDataSourceTests {

    private AppDataSource dataSource = AppDataSource.getDataSourceInstance();

    @Test
    public void getNewConnectionTest() throws SQLException {
        try( Connection connection = dataSource.getNewConnection() ) {
            assertTrue(connection.isValid(1));
            assertFalse(connection.isClosed());
        }
    }

    @Test
    public void shouldRunSelect() throws SQLException {
        try( Connection connection = dataSource.getNewConnection() ) {
            String query = "SELECT * FROM con";
            PreparedStatement statement = connection.prepareStatement(query);
            boolean hasResult = statement.execute();
            assertTrue(hasResult);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int value = resultSet.getInt("value");
//            assertEquals(0, value);
        }
    }
}

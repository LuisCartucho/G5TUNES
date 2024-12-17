package easv.g5tunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {


    private Connection connection;

    public DBConnection() throws SQLException {
        reconnect();
    }

    private void reconnect() throws SQLException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setDatabaseName("G5TUNES");
            ds.setUser("CSe2024b_e_18");
            ds.setPassword("CSe2024bE18!24");
            ds.setServerName("EASV-DB4");
            ds.setPortNumber(1433);
            ds.setTrustServerCertificate(true);

            this.connection = ds.getConnection();
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Reconnecting to the database...");
            reconnect();
        }
        return connection;
    }
}










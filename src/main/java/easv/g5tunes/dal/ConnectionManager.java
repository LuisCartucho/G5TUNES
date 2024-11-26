package easv.g5tunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private final SQLServerDataSource ds;

    public ConnectionManager() {
        ds = new SQLServerDataSource();
        ds.setServerName("EASV-DB4");
        ds.setDatabaseName("G5TUNES");
        ds.setPortNumber(1433);
        ds.setUser("CSe2024b_e_18");
        ds.setPassword("CSe2024bE18!24");
        ds.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

package easv.g5tunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.g5tunes.be.Songs;
import easv.g5tunes.dal.db.ConnectionManager;
import easv.g5tunes.exceptions.MyTuneExceptions;

import javax.swing.text.html.ListView;
import java.sql.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SongsDAO implements ISongsDAO {
    public List<Songs> getAll() throws MyTuneExceptions {
        List<Songs> songs = new ArrayList<>();

        SQLServerDataSource ds;
        ds = new SQLServerDataSource();
        ds.setDatabaseName("G5TUNES");
        ds.setUser("CSe2024b_e_18");
        ds.setPassword("CSe2024bE18!24"); // Use your own password
        ds.setServerName("EASV-DB4");
        ds.setPortNumber(1433);
        ds.setTrustServerCertificate(true);
        try {
            Connection c = ds.getConnection();
            String sql = "SELECT * FROM dbo.Songs";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { //while there are rows
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String filePath = rs.getString("filePath");
                int duration = rs.getInt("duration");
                Songs songs1 = new Songs(id, title, artist, filePath, duration);
                songs1.add(songs);
            }

        } catch (SQLServerException e) {
            throw new MyTuneExceptions(e);
        } catch (SQLException e) {
            throw new MyTuneExceptions(e);
        }

        return songs;
    }
}










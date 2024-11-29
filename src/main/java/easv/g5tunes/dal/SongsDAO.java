package easv.g5tunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.g5tunes.be.Songs;
import easv.g5tunes.exceptions.MyTuneExceptions;

import javax.swing.text.html.ListView;
import java.sql.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDAO {
    private static final String URL = "jdbc:sqlserver://10.176.111.34:1433;databaseName=G5TUNES";
    private static final String USER = "CSe2024b_e_18";
    private static final String PASSWORD = "CSe2024bE18!24";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Songs> getAllSongs() {
        List<Songs> songs = new ArrayList<>();
        String sql = "SELECT ID ,Title, Artist";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Songs song = new Songs(
                        rs.getInt("Id"),
                        rs.getString("Title"),
                        rs.getString("Artist"),
                        rs.getString("File Path"),
                        rs.getInt("Duration")

                );
                songs.add(song);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return songs;
    }
}







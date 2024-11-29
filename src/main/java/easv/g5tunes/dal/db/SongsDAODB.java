package easv.g5tunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.g5tunes.be.Songs;
import easv.g5tunes.bll.ConnectionManager;
import easv.g5tunes.dal.ISongsDAO;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SongsDAODB implements ISongsDAO {

    private ConnectionManager con = new ConnectionManager();

    public List<Songs> getAll() throws MyTuneExceptions {
        List<Songs> songs = new ArrayList<>();
        try {
            Connection c = con.getConnection();
            String sql = "SELECT * FROM Songs";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String filePath = rs.getString("file path");
                int duration = rs.getInt("duration");
                Songs songs1 = new Songs( id, title, artist, filePath, duration);
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


package easv.g5tunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.g5tunes.be.Songs;
import easv.g5tunes.dal.ISongsDAO;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
public class SongsDAODB implements ISongsDAO {

    private DBConnection con = new DBConnection();

    public List<Songs> getAll() throws MyTuneExceptions {
        List<Songs> songs = new ArrayList<>();
        try {
            Connection c = con.getConnection();
            String sql = "SELECT * FROM Songs";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                //String filePath = rs.getString("filePath");
                Songs songs1 = new Songs(title, artist, );
                songs.add(songs1);
            }
        } catch (SQLServerException e) {
            throw new MyTuneExceptions(e);
        } catch (SQLException e) {
            throw new MyTuneExceptions(e);
        }
        return songs;
    }

    @Override
    public Songs add(Songs songs) throws MyTuneExceptions {
        return null;
    }

    @Override
    public void delete(Songs songs) throws MyTuneExceptions {

    }

    @Override
    public void update(Songs songs) throws MyTuneExceptions {

    }


}
   */

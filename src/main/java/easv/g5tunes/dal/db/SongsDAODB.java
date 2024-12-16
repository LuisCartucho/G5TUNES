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


public class SongsDAODB {

        private DBConnection con = new DBConnection();

        public void addSongs(List<Songs> songsList) throws SQLException {
            String sql = "INSERT INTO Songs (title, artist, filePath) VALUES (?, ?, ?)";
            try (Connection connection = con.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                for (Songs song : songsList) {
                    stmt.setString(1, song.getTitle());
                    stmt.setString(2, song.getArtist());
                    stmt.setString(3, song.getFilePath());
                    stmt.addBatch(); // Add each song to a batch
                }

                stmt.executeBatch(); // Execute all insertions at once
                System.out.println("Songs saved to the database successfully!");
            } catch (SQLException e) {
                System.err.println("Error saving songs to the database: " + e.getMessage());
                throw e;
            }
        }
    }
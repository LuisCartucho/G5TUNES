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

        // Method to check if a song exists in the database
        private boolean songExists(Songs song) throws SQLException {
            String sql = "SELECT COUNT(*) FROM Songs WHERE title = ? AND artist = ? AND filePath = ?";
            try (Connection connection = con.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, song.getTitle());
                stmt.setString(2, song.getArtist());
                stmt.setString(3, song.getFilePath());

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count > 0, song exists
                }
            }
            return false;
        }

        // Method to add songs to the database (avoiding duplicates)
        public void addSongs(List<Songs> songsList) throws SQLException {
            String sql = "INSERT INTO Songs (title, artist, filePath) VALUES (?, ?, ?)";
            try (Connection connection = con.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                for (Songs song : songsList) {
                    // Check if the song already exists before inserting
                    if (!songExists(song)) {
                        stmt.setString(1, song.getTitle());
                        stmt.setString(2, song.getArtist());
                        stmt.setString(3, song.getFilePath());
                        stmt.addBatch(); // Add to batch
                    } else {
                        System.out.println("Song already exists: " + song.getTitle());
                    }
                }

                stmt.executeBatch(); // Execute all insertions at once
                System.out.println("Songs saved to the database successfully!");
            } catch (SQLException e) {
                System.err.println("Error saving songs to the database: " + e.getMessage());
                throw e;
            }
        }

    public boolean updateSong(Songs updatedSong, String originalTitle, String originalArtist) throws SQLException {
        String sql = "UPDATE songs SET title = ?, artist = ?, filePath = ? WHERE title = ? AND artist = ?";
        try (Connection connection = con.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, updatedSong.getTitle());
            pstmt.setString(2, updatedSong.getArtist());
            pstmt.setString(3, updatedSong.getFilePath());
            pstmt.setString(4, originalTitle);
            pstmt.setString(5, originalArtist);
            return pstmt.executeUpdate() > 0; // Returns true if the update was successful
        }
    }

    public boolean deleteSong(String title, String artist) throws SQLException {
        String sql = "DELETE FROM songs WHERE title = ? AND artist = ?";
        try (Connection connection = con.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            return pstmt.executeUpdate() > 0; // Returns true if the deletion was successful
        }
    }

}
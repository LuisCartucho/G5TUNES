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
import java.sql.*;


public class SongsDAODB  {

         private DBConnection con = new DBConnection();

    public SongsDAODB() throws SQLException {
    }

    public List<Songs> getAllSongs() {
        List<Songs> allSongs = new ArrayList<>();
        String sql = "SELECT * FROM Songs";

        try (Statement statement = con.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String filePath = resultSet.getString("filePath");

                Songs song = new Songs(title, artist, filePath);
                allSongs.add(song);
            }
            System.out.println("Fetched Songs: " + allSongs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allSongs;
    }


    // Method to check if a song exists in the database
        private boolean songExists(Songs song) throws SQLException {
            String sql = "SELECT 1 FROM Songs WHERE title = ? AND artist = ? AND filePath = ?";
            try (Connection connection = con.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, song.getTitle());
                stmt.setString(2, song.getArtist());
                stmt.setString(3, song.getFilePath());

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Returns true if a song exists
                }
            }
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

                int[] result = stmt.executeBatch(); // Execute all insertions at once
                System.out.println("Songs saved to the database successfully!");

                // Check if all songs were inserted
                int successCount = 0;
                for (int res : result) {
                    if (res == PreparedStatement.SUCCESS_NO_INFO || res > 0) {
                        successCount++;
                    }
                }
                System.out.println(successCount + " songs were successfully added.");
            } catch (SQLException e) {
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

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Song updated successfully.");
                return true;
            } else {
                System.out.println("No song found to update.");
                return false;
            }
        }
    }

    public boolean deleteSong(String title, String artist) throws SQLException {
        String sql = "DELETE FROM songs WHERE title = ? AND artist = ?";
        try (Connection connection = con.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Song deleted successfully.");
                return true;
            } else {
                System.out.println("No song found to delete.");
                return false;
            }
        }
    }
}
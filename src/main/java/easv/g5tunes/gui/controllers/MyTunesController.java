package easv.g5tunes.gui.controllers;

import easv.g5tunes.be.Songs;
import easv.g5tunes.bll.SongService;
import easv.g5tunes.bll.FilterService;
import easv.g5tunes.dal.SongsDAO;
import easv.g5tunes.dal.db.DBConnection;
import easv.g5tunes.dal.db.SongsDAODB;
import easv.g5tunes.exceptions.MyTuneExceptions;
import easv.g5tunes.gui.model.SongsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class MyTunesController implements Initializable {

    private final SongsModel songsModel = new SongsModel();
    SongService songService = new SongService();
    private SongsDAO songsDAO = new SongsDAO();
    private MediaPlayer currentMediaPlayer;
    private final FilterService filterService = new FilterService();
    private boolean isFilterActive = false; // Track the current state of the button

    @FXML
    private Button btnSongEdit;
    @FXML
    private Label txtSongName;
    @FXML
    private ListView<String> lstViewPlaylists;
    @FXML
    private ListView<String> lstViewSongonPlaylist;
    @FXML
    private ListView<Songs> lstViewSongs;
    @FXML
    private TextField fieldFilterSearch;
    @FXML
    private Slider audioVolume;
    @FXML
    private ProgressBar audioProgressBar;
    @FXML
    private ToggleButton btnPlayPause;
    @FXML
    public Button btnNewSong;
    @FXML
    private Button btnFilter;

    private int currentSongIndex = -1;

    private SongsDAODB dao;

    public MyTunesController() throws SQLException {
    }


    public void refreshListView() {
        lstViewSongs.refresh(); // Ensure the updated data appears in ListView
    }

    public void deleteSongFromListView() {
        Songs selectedSongs = lstViewSongs.getSelectionModel().getSelectedItem();
        lstViewSongs.getItems().remove(selectedSongs);
    }

    public void addSongToListView(Songs song) {
        lstViewSongs.getItems().add(song);
    }

    public void onClickFilterSearch(ActionEvent actionEvent) {
        if (isFilterActive) {
            // Clear filter
            lstViewSongs.setItems(FXCollections.observableArrayList(songsModel.getAllSongs()));
            fieldFilterSearch.clear();
            btnFilter.setText("Filter");
            isFilterActive = false;
        } else {
            // Apply filter
            String filterQuery = fieldFilterSearch.getText().trim().toLowerCase();
            List<Songs> filteredSongs = filterService.filterSongs(
                    songsModel.getAllSongs(), // Get all songs
                    filterQuery // Pass query to the filter method
            );
            System.out.println("Filtered Songs Count: " + filteredSongs.size());
            lstViewSongs.setItems(FXCollections.observableArrayList(filteredSongs));
            btnFilter.setText("Clear");
            isFilterActive = true;
        }
    }

    public void addPlaylistToListView(String playlistName) {
        lstViewPlaylists.getItems().add(playlistName);
    }

    public void onClickPlaylistsNew(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/easv/g5tunes/newEditPlaylist.fxml"));
        Parent scene = loader.load();

        NewEditPlaylistController controller = loader.getController();
        controller.setMainController(this); // Pass the main controller to the new controller

        Stage stage = new Stage();
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.setTitle("New Playlist");
        stage.centerOnScreen();

        stage.show();

    }

    public void updatePlaylistName(String oldName, String newName) {
        ObservableList<String> playlist = lstViewPlaylists.getItems();

        int index = playlist.indexOf(oldName);
        if (index != -1) {
            playlist.set(index, newName);
        }
    }

    public void onClickPlaylistEdit(ActionEvent actionEvent) throws IOException {

        String selectedPlaylist = lstViewPlaylists.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/easv/g5tunes/playlistEditMenu.fxml"));
            Parent scene = loader.load();

            playListEditMenuController controller = loader.getController();
            controller.setMainController(this);
            controller.setPlaylistName(selectedPlaylist);

            Stage stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.setTitle("Edit playlist");
            stage.centerOnScreen();

            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a playlist to edit.");
            alert.showAndWait();
        }

    }

    public void onClickPlaylistsDelete(ActionEvent actionEvent) {

        // Get the selected playlist from the ListView
        String selectedPlaylist = lstViewPlaylists.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            try {
                //  Retrieve the database connection
                Connection conn = dbc.getConnection(); // Fetch actual Connection object from dbc

                //  Prepare the SQL query
                String deleteSQL = "DELETE FROM Playlists WHERE playlistName = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
                pstmt.setString(1, selectedPlaylist);

                //  Execute the query
                int affectedRows = pstmt.executeUpdate();
                pstmt.close();

                //  Check if deletion was successful
                if (affectedRows > 0) {

                    lstViewPlaylists.getItems().remove(selectedPlaylist);
                    showAlert("Success", "Playlist '" + selectedPlaylist + "' has been deleted.");
                } else {
                    showAlert("Error", "Could not delete playlist.");
                }
            } catch (SQLException e) {
                showAlert("Database Error", "Error deleting playlist: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "Please select a playlist to delete.");
        }
    }


    public void onClickSongsNew(ActionEvent actionEvent) {
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );
        File selectedFile = filechooser.showOpenDialog(null);

        if (selectedFile != null) {
            Songs newSong = songService.extractSongMetadata(selectedFile);
            songService.addSong(newSong);
            lstViewSongs.getItems().add(newSong);
        }
    }

    public void onClickSongsEdit(ActionEvent actionEvent) throws MyTuneExceptions, IOException {

        Songs selectedSongs = lstViewSongs.getSelectionModel().getSelectedItem();
        if (selectedSongs != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/easv/g5tunes/addNewSong.fxml"));
            Parent root = loader.load();

            AddNewSongController addNewSongController = loader.getController();
            addNewSongController.setSongs(selectedSongs); // Pass selected song to Edit controller
            addNewSongController.setMainController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            System.out.println("No song selected!");
        }
    }

    public void onClickSongsDelete(ActionEvent actionEvent) {
        // Get the selected song from the list view
        Songs selectedSongs = lstViewSongs.getSelectionModel().getSelectedItem();

        if (selectedSongs != null) {
            // Confirm deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Song");
            confirmationAlert.setHeaderText("Are you sure you want to delete this song?");
            confirmationAlert.setContentText("Song: " + selectedSongs.getTitle());

            // Wait for the user's response
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Perform the deletion
                lstViewSongs.getItems().remove(selectedSongs); // Removes the song from the ListView
                // Optionally, remove it from the underlying data source as well
                //deleteSongFromDatabase(selectedSongs);

                System.out.println("Song deleted: " + selectedSongs.getTitle());
            }
        } else {
            // If no song is selected, show a warning
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No song selected");
            warningAlert.setContentText("Please select a song to delete.");
            warningAlert.showAndWait();
        }
    }

    public void onClickClose(ActionEvent actionEvent) {

        Stage currentStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void OnClickSongonPlaylistDelete(ActionEvent actionEvent) {

            // Get the selected song title from the ListView (string)
            String selectedSongTitle = lstViewSongonPlaylist.getSelectionModel().getSelectedItem();

            if (selectedSongTitle == null) {
                showAlert("Error", "No song selected for deletion!");
                return;
            }

            // Retrieve the song ID from the database based on the selected song title
            int songId = getSongIdByTitle(selectedSongTitle);

            if (songId == -1) {
                showAlert("Error", "Song not found in the database!");
                return;
            }

            // Confirmation prompt before deletion
            if (!confirmDelete(selectedSongTitle)) {
                return;  // User canceled the deletion
            }

            // SQL query to delete the selected song from the playlist using the song_id
            String deleteSongSQL = "DELETE FROM PlaylistSong WHERE songs_id = ?";

            try (Connection con = dbc.getConnection();
                 PreparedStatement stmt = con.prepareStatement(deleteSongSQL)) {

                // Set the song_id parameter (not the song title)
                stmt.setInt(1, songId);

                // Execute the query
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert("Success", "Song '" + selectedSongTitle + "' removed from the playlist!");

                    // Remove the song from the ListView (UI only)
                    lstViewSongonPlaylist.getItems().remove(selectedSongTitle);
                } else {
                    showAlert("Error", "Failed to delete the song.");
                }
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete song: " + e.getMessage());
            }
        }

// Helper method to retrieve the song ID by title from the database
        private int getSongIdByTitle(String songTitle) {
            String query = "SELECT id FROM Songs WHERE title = ?";
            try (Connection con = dbc.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {

                // Set the song title parameter
                stmt.setString(1, songTitle);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");  // Return the song ID
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;  // Return -1 if the song is not found
        }

    private boolean confirmDelete(String songName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete the song from the PlayList?");
        alert.setContentText("Song: " + songName);

        // Show the alert and wait for the user response
        return alert.showAndWait().filter(response -> response.getText().equals("OK")).isPresent();
    }


    public void onClickPlayStop(ActionEvent actionEvent) {
        Songs selectedSong = lstViewSongs.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            showAlert("No Song Selected", "Please select a song to play.");
            return;
        }

        String musicFilePath = selectedSong.getFilePath();
        File musicFile = new File(musicFilePath);
        String musicFileURI = musicFile.toURI().toString();

        // Check if MediaPlayer exists and if the selected song is already playing
        if (currentMediaPlayer != null && currentMediaPlayer.getMedia().getSource().equals(musicFileURI)) {
            if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                currentMediaPlayer.pause();
                btnPlayPause.setSelected(false);
            } else {
                currentMediaPlayer.play();
                btnPlayPause.setSelected(true);
            }
        } else {
            // Store the current volume level
            double lastVolumeLevel = audioVolume.getValue();

            // Stop and dispose of previous MediaPlayer
            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
                currentMediaPlayer.dispose();
            }

            try {
                // Create new MediaPlayer
                Media media = new Media(musicFileURI);
                currentMediaPlayer = new MediaPlayer(media);

                // Restore volume level
                currentMediaPlayer.setVolume(lastVolumeLevel / 100.0);
                audioVolume.setValue(lastVolumeLevel); // Update the slider visually

                // Bind slider to MediaPlayer volume
                audioVolume.valueProperty().addListener((obs, oldVal, newVal) -> {
                    double volume = newVal.doubleValue() / 100.0; // Scale slider [0-100] to MediaPlayer [0-1]
                    currentMediaPlayer.setVolume(volume);
                });

                // Update progress bar
                currentMediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                    if (currentMediaPlayer.getTotalDuration() != null) {
                        double progress = newTime.toSeconds() / currentMediaPlayer.getTotalDuration().toSeconds();
                        audioProgressBar.setProgress(progress);
                    }
                });

                currentMediaPlayer.play();
                btnPlayPause.setSelected(true);
                txtSongName.setText(selectedSong.getTitle());
            } catch (Exception e) {
                showAlert("Playback Error", "An error occurred while playing: " + e.getMessage());
            }
        }

    }

    public void OnClickSongonPlaylistScrollUp(ActionEvent actionEvent) {

        ObservableList<String> songsInPlaylist = lstViewSongonPlaylist.getItems();
        int selectedIndex = lstViewSongonPlaylist.getSelectionModel().getSelectedIndex();

        if (selectedIndex > 0) {
            // Swap the song with the previous one
            String selectedSong = songsInPlaylist.get(selectedIndex);
            songsInPlaylist.set(selectedIndex, songsInPlaylist.get(selectedIndex - 1));
            songsInPlaylist.set(selectedIndex - 1, selectedSong);

            // Re-select the moved song (optional)
            lstViewSongonPlaylist.getSelectionModel().select(selectedIndex - 1);

            // Optional: Update the song order in the database if necessary
            updateSongOrderInDatabase();
        }
    }

    public void OnClickSongonPlaylistScrollDown(ActionEvent actionEvent) {

        ObservableList<String> songsInPlaylist = lstViewSongonPlaylist.getItems();
        int selectedIndex = lstViewSongonPlaylist.getSelectionModel().getSelectedIndex();

        if (selectedIndex < songsInPlaylist.size() - 1) {
            // Swap the song with the next one
            String selectedSong = songsInPlaylist.get(selectedIndex);
            songsInPlaylist.set(selectedIndex, songsInPlaylist.get(selectedIndex + 1));
            songsInPlaylist.set(selectedIndex + 1, selectedSong);

            // Re-select the moved song (optional)
            lstViewSongonPlaylist.getSelectionModel().select(selectedIndex + 1);

            // Optional: Update the song order in the database if necessary
            updateSongOrderInDatabase();
        }
    }

    private int currentPlaylistId = -1;

    private void updateSongOrderInDatabase() {
        ObservableList<String> songsInPlaylist = lstViewSongonPlaylist.getItems();

        // Loop through all songs in the list and update their order in the database
        for (int i = 0; i < songsInPlaylist.size(); i++) {
            String songTitle = songsInPlaylist.get(i);

            // Get the song_id based on song title
            try {
                int songId = getSongIdByTitle(songTitle);

                // Update the order for this song in the PlaylistSong table
                String updateQuery = "UPDATE PlaylistSong SET song_order = ? WHERE songs_id = ? AND playlist_id = ?";
                try (Connection conn = dbc.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setInt(1, i + 1);  // Order starts from 1
                    stmt.setInt(2, songId);
                    stmt.setInt(3, currentPlaylistId);  // Assuming you have the current playlist ID
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating song order in the database: " + e.getMessage());
            }
        }
    }


    public void loadSongsFromFolder(String folderPath) {
        // Fetch songs using SongsDAO
        List<Songs> songs = songsDAO.getSongsFromFolder(folderPath);

        // Clear the ListView and add songs
        lstViewSongs.getItems().clear();
        lstViewSongs.getItems().addAll(songs);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load songs from the "musics" folder on desktop
        String folderPath = "C:\\Users\\Ali Emre\\Desktop\\mp3 files for myTunes"; // Path to your folder
        loadSongsFromFolder(folderPath);
        loadPlaylistsFromDatabase();
        lstViewPlaylists.setOnMouseClicked(this::onPlaylistClicked);
        try {
            dao = new SongsDAODB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Automatically save the songs in lstViewSongs to the database
        saveSongsToDatabase();

        audioVolume.setMin(0);
        audioVolume.setMax(100);
        audioVolume.setValue(50); // Default volume at 50%
        audioProgressBar.setProgress(0.0);
    }

    DBConnection dbc = new DBConnection();

    public void loadPlaylistsFromDatabase() {
        String selectPlaylistsSQL = "SELECT playlistName FROM Playlists";

        try (Connection con = dbc.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(selectPlaylistsSQL)) {

            // Clear the current ListView
            lstViewPlaylists.getItems().clear();

            // Add each playlist name to the ListView
            while (rs.next()) {
                String playlistName = rs.getString("playlistName");
                lstViewPlaylists.getItems().add(playlistName);
            }

        } catch (Exception e) {
            System.err.println("Error loading playlists: " + e.getMessage());
        }
    }

    private void saveSongsToDatabase() {
        try {
            // Fetch all songs displayed in the ListView
            ObservableList<Songs> songsObservableList = lstViewSongs.getItems();
            List<Songs> songsList = new ArrayList<>(songsObservableList);

            // Save songs to the database
            if (!songsList.isEmpty()) {
                dao.addSongs(songsList);
                System.out.println("Songs successfully saved to the database.");
            } else {
                System.out.println("No songs to save.");
            }
        } catch (SQLException e) {
            System.err.println("Error saving songs to the database: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void playSong(Songs song) {
        if (song == null) {
            showAlert("No Song Selected", "Please select a song to play.");
            return;
        }

        String musicFilePath = song.getFilePath();
        File musicFile = new File(musicFilePath);

        if (!musicFile.exists()) {
            showAlert("File not found", "The file does not exist: " + musicFilePath);
            return;
        }

        String musicFileURI = musicFile.toURI().toString();

        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
            currentMediaPlayer.dispose();
        }

        try {
            Media media = new Media(musicFileURI);
            currentMediaPlayer = new MediaPlayer(media);

            currentMediaPlayer.setVolume(audioVolume.getValue());
            currentMediaPlayer.play();

            txtSongName.setText(song.getTitle());
            btnPlayPause.setSelected(true);
        } catch (Exception e) {
            showAlert("Playback Error", "An error occurred: " + e.getMessage());
        }
    }

    public void onClickBtnNext(ActionEvent actionEvent) {
        ObservableList<Songs> songsList = lstViewSongs.getItems();

        if (songsList.isEmpty()) {
            showAlert("No songs", "There are no songs in the list.");
            return;
        }

        //Incrementing the index
        currentSongIndex = (currentSongIndex + 1) % songsList.size();

        Songs nextSong = songsList.get(currentSongIndex);
        lstViewSongs.getSelectionModel().select(nextSong);
        playSong(nextSong);
    }

    public void onClickBtnPrevious(ActionEvent actionEvent) {
        ObservableList<Songs> songsList = lstViewSongs.getItems();

        if (songsList.isEmpty()) {
            showAlert("No Songs", "There are no songs in the list.");
            return;
        }

        // Decrement the index
        currentSongIndex = (currentSongIndex - 1 + songsList.size()) % songsList.size();

        Songs previousSong = songsList.get(currentSongIndex);
        lstViewSongs.getSelectionModel().select(previousSong);
        playSong(previousSong);
    }
    @FXML
    private void onClickAddSongsToPlaylist(ActionEvent event) {
        // Get the selected playlist
        String selectedPlaylist = lstViewPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            System.out.println("No playlist selected");
            return;
        }

        // Get the selected songs from the list view
        ObservableList<Songs> selectedSongs = lstViewSongs.getSelectionModel().getSelectedItems();
        if (selectedSongs.isEmpty()) {
            System.out.println("No songs selected");
            return;
        }

        try {
            // Get the playlist_id based on the selected playlist name
            int playlistId = getPlaylistId(selectedPlaylist);
            if (playlistId == -1) {
                System.out.println("Playlist not found in the database");
                return;
            }

            // For each selected song, get its song_id and add it to the playlist
            for (Songs song : selectedSongs) {
                int songId = getSongId(song);
                if (songId == -1) {
                    System.out.println("Song not found in the database");
                    continue;
                }
                if (isSongInPlaylist(playlistId, songId)) {
                    System.out.println("Song '" + song.getTitle() + "' already exists in the playlist");
                    continue; // Skip duplicates
                }

                addSongToPlaylist(playlistId, songId);
                lstViewSongonPlaylist.getItems().add(song.getTitle()); // Update UI to show song in the playlist
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding songs to playlist: " + e.getMessage());
        }
    }

        private boolean isSongInPlaylist(int playlistId, int songId) throws SQLException {
            String query = "SELECT 1 FROM PlaylistSong WHERE playlist_id = ? AND songs_id = ?";
            try (Connection conn = dbc.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, playlistId);
                stmt.setInt(2, songId);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // If a row is found, the song is already in the playlist
                }
            }
        }



    // Retrieve the playlist_id from the database based on playlist name
    private int getPlaylistId(String playlistName) throws SQLException {
        String query = "SELECT id FROM Playlists WHERE playlistName = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playlistName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Return the ID if found
                }
            }
        }
        // Return -1 or throw an exception if no playlist is found
        return -1; // Indicates that the playlist does not exist
    }

    // Retrieve the song_id from the database based on song name
    private int getSongId(Songs song) throws SQLException {
        String query = "SELECT id FROM Songs WHERE title = ?";
        try (Connection conn = dbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, song.getTitle());  // Assuming 'getTitle()' method exists in Songs class
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; // Song not found
    }

    // Add a song to the playlist in the playlistSongs table
    private void addSongToPlaylist(int playlistId, int songId) throws SQLException {
        String query = "INSERT INTO PlaylistSong (playlist_id, songs_id) VALUES (?, ?)";
        try (Connection conn = dbc.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            stmt.executeUpdate();
        }
    }

    private ObservableList<String> getSongsInPlaylist(int playlistId) {
        ObservableList<String> songTitles = FXCollections.observableArrayList();
        String query = "SELECT s.title FROM Songs s " +
                "INNER JOIN PlaylistSong ps ON s.id = ps.songs_id " +  // Check the column names
                "WHERE ps.playlist_id = ?";  // Check the column names

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, playlistId);  // Set the playlist ID parameter
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                songTitles.add(title);  // Add song title to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading songs for playlist: " + e.getMessage());
        }
        return songTitles;
    }

    @FXML
    private void onPlaylistClicked(MouseEvent event) {
        // Get the selected playlist name from lstViewPlaylists
        String selectedPlaylist = lstViewPlaylists.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            // Get the playlist ID based on the selected playlist name
            currentPlaylistId = getPlaylistIdByName(selectedPlaylist);

            // Fetch the songs for this playlist
            ObservableList<String> songs = getSongsInPlaylist(currentPlaylistId);

            // Clear the previous songs from the ListView
            lstViewSongonPlaylist.getItems().clear();

            // Add the new songs to the ListView
            lstViewSongonPlaylist.setItems(songs);
        }
    }

    public int getPlaylistIdByName(String playlistName) {
        int playlistId = -1;
        String query = "SELECT id FROM Playlists WHERE playlistName = ?";  // Adjust if necessary

        try (Connection conn = dbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, playlistName);  // Set the playlist name
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                playlistId = rs.getInt("id");  // Use the correct column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting playlist ID: " + e.getMessage());
        }
        return playlistId;
    }
}












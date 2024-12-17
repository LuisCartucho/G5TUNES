package easv.g5tunes.gui.controllers;

import easv.g5tunes.be.Songs;
import easv.g5tunes.bll.SongService;
import easv.g5tunes.bll.FilterService;
import easv.g5tunes.dal.SongsDAO;
import easv.g5tunes.dal.db.DBConnection;
import easv.g5tunes.dal.db.SongsDAODB;
import easv.g5tunes.exceptions.MyTuneExceptions;
import easv.g5tunes.gui.model.SongsModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {

    private final SongsModel songsModel = new SongsModel();
    SongService songService = new SongService();
    private SongsDAO songsDAO = new SongsDAO();
    private MediaPlayer currentMediaPlayer;

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
    }

    public void OnClickSongonPlaylistScrollDown(ActionEvent actionEvent) {
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

    public void onClickFastForward(ActionEvent actionEvent) {
    }

    public void OnClickSongonPlaylistScrollUp(ActionEvent actionEvent) {
    }

    public void onClickAddSongsToPlaylist(ActionEvent actionEvent) {
    }

    public void onClickRewind(ActionEvent actionEvent) {
    }

    private void showAlertWindow(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.showAndWait();
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
        dao = new SongsDAODB();
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
}











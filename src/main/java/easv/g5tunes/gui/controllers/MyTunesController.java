package easv.g5tunes.gui.controllers;

import easv.g5tunes.be.Songs;
import easv.g5tunes.bll.SongService;
import easv.g5tunes.dal.SongsDAO;
import easv.g5tunes.exceptions.MyTuneExceptions;
import easv.g5tunes.gui.model.SongsModel;
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

    private MediaPlayer mediaPlayer;
    private Media currentMedia;

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

    public void onClickPlaylistsEdit(ActionEvent actionEvent) {
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a song to play.");
            alert.showAndWait();
            return;
        }

        String musicFilePath = selectedSong.getFilePath();
        File musicFile = new File(musicFilePath);

        if (!musicFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The selected file does not exist: " + musicFilePath);
            alert.showAndWait();
            return;
        }

        String musicFileURI = musicFile.toURI().toString();

        if (currentMediaPlayer != null &&
                currentMediaPlayer.getStatus() != MediaPlayer.Status.DISPOSED &&
                currentMediaPlayer.getMedia().getSource().equals(musicFileURI)) {

            if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                currentMediaPlayer.pause();
                btnPlayPause.setSelected(false);
            } else {
                currentMediaPlayer.play();
                btnPlayPause.setSelected(true);
            }
            return;
        }

        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
            currentMediaPlayer.dispose();
        }

        try {
            Media musicFileMedia = new Media(musicFileURI);
            currentMediaPlayer = new MediaPlayer(musicFileMedia);

            // Set initial volume to 50%
            double initialVolume = 0.5;
            audioVolume.setValue(initialVolume);
            currentMediaPlayer.setVolume(initialVolume);

            // Update MediaPlayer volume dynamically when the slider changes
            audioVolume.valueProperty().addListener((obs, oldVal, newVal) -> {
                currentMediaPlayer.setVolume(newVal.doubleValue());
            });

            currentMediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (currentMediaPlayer.getTotalDuration() != null) {
                    double progress = newTime.toSeconds() / currentMediaPlayer.getTotalDuration().toSeconds();
                    audioProgressBar.setProgress(progress);
                }
            });

            currentMediaPlayer.setOnEndOfMedia(() -> {
                btnPlayPause.setSelected(false);
                audioProgressBar.setProgress(0);
            });

            currentMediaPlayer.play();
            btnPlayPause.setSelected(true);
            txtSongName.setText(selectedSong.getTitle());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Playback Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while trying to play the song: " + e.getMessage());
            alert.showAndWait();
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
    }

    }






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

    public void onClickPlaylistsNew(ActionEvent actionEvent) {

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
            // Show a warning if no song is selected
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No song selected");
            warningAlert.setContentText("Please select a song to play.");
            warningAlert.showAndWait();
            return;
        }

        String musicFileString = selectedSong.getFilePath();
        File musicFile = new File(musicFileString);

        if (!musicFile.exists()) {
            // Show an error if the file doesn't exist
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("File Not Found");
            errorAlert.setHeaderText("Music file not found");
            errorAlert.setContentText("The selected file does not exist: " + musicFileString);
            errorAlert.showAndWait();
            return;
        }

        // Stop and dispose of the existing MediaPlayer, if any
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null; // Explicitly set to null to ensure a fresh instance
        }

        try {
            // Create a new Media instance
            currentMedia = new Media(musicFile.toURI().toString());

            // Create a new MediaPlayer for the selected song
            mediaPlayer = new MediaPlayer(currentMedia);

            // Play the selected song
            mediaPlayer.play();
            btnPlayPause.setSelected(true); // Set the toggle button to "playing"

            // Bind the progress bar to the song's progress
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (mediaPlayer.getTotalDuration() != null) {
                    double progress = newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                    audioProgressBar.setProgress(progress);
                }
            });

            // Reset play/pause and progress bar when the song ends
            mediaPlayer.setOnEndOfMedia(() -> {
                btnPlayPause.setSelected(false);
                audioProgressBar.setProgress(0);
            });

        } catch (Exception e) {
            // Handle unexpected issues with MediaPlayer creation
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Playback Error");
            errorAlert.setHeaderText("An error occurred while trying to play the song.");
            errorAlert.setContentText("Error details: " + e.getMessage());
            errorAlert.showAndWait();
        }

//        Songs selectedSong = lstViewSongs.getSelectionModel().getSelectedItem();
//
//        if (selectedSong == null) {
//            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
//            warningAlert.setTitle("No Selection");
//            warningAlert.setHeaderText("No song selected");
//            warningAlert.setContentText("Please select a song to play.");
//            warningAlert.showAndWait();
//            return;
//        }
//
//        String musicFileString = selectedSong.getFilePath();
//        File musicFile = new File(musicFileString);
//
//        if (!musicFile.exists()) {
//            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//            errorAlert.setTitle("File Not Found");
//            errorAlert.setHeaderText("Music file not found");
//            errorAlert.setContentText("The selected file does not exist: " + musicFileString);
//            errorAlert.showAndWait();
//            return;
//        }
//
//        // Stop and dispose of the existing MediaPlayer if a new song is selected
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.dispose();
//        }
//
//        // Create a new MediaPlayer for the selected song
//        currentMedia = new Media(musicFile.toURI().toString());
//        mediaPlayer = new MediaPlayer(currentMedia);
//
//        // Update the play/pause toggle button
//        btnPlayPause.setOnAction(event -> {
//            if (btnPlayPause.isSelected()) {
//                mediaPlayer.play();
//            } else {
//                mediaPlayer.pause();
//            }
//        });
//
//        // Start playing the new song
//        mediaPlayer.play();
//        btnPlayPause.setSelected(true); // Update button state to "playing"
//
//        // Update the progress bar based on the song's progress
//        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
//            if (mediaPlayer.getTotalDuration() != null) {
//                double progress = newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
//                audioProgressBar.setProgress(progress);
//            }
//        });
//
//        // Handle the end of the song to reset the button and progress bar
//        mediaPlayer.setOnEndOfMedia(() -> {
//            btnPlayPause.setSelected(false);
//            audioProgressBar.setProgress(0);
//        });

        //88
//        Songs selectedSong = lstViewSongs.getSelectionModel().getSelectedItem();
//
//        if (selectedSong == null) {
//
//            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
//            warningAlert.setTitle("No Selection");
//            warningAlert.setHeaderText("No song selected");
//            warningAlert.setContentText("Please select a song to play.");
//            warningAlert.showAndWait();
//            return;
//        }
//
//
//        String musicFileString = selectedSong.getFilePath();
//        File musicFile = new File(musicFileString);
//
//        if(!musicFile.exists()) {
//
//            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//            errorAlert.setTitle("File Not Found");
//            errorAlert.setHeaderText("Music file not found");
//            errorAlert.setContentText("The selected file does not exist:" + musicFileString);
//            errorAlert.showAndWait();
//            return;
//        }
//
//        // Stop and dispose of the existing MediaPlayer, if any
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.dispose();
//        }
//
//
//        // This creates a new MediaPlayer for the selected song
//        currentMedia = new Media(musicFile.toURI().toString());
//        mediaPlayer = new MediaPlayer(currentMedia);
//
//        //Handles play/pause toggle
//        btnPlayPause.setOnAction(event -> {
//            if (btnPlayPause.isSelected()) {
//                mediaPlayer.play();
//            } else {
//                mediaPlayer.pause();
//            }
//        });
//
//        // Start playing the new song
//        mediaPlayer.play();
//        btnPlayPause.setSelected(true); // Update button state to "playing"
//
//        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
//            double progress = newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
//            audioProgressBar.setProgress(progress);
//        });




        //54

        //        String musicFileString = lstViewSongs.getSelectionModel().getSelectedItem().getFilePath();
//        Media musicFileMedia = new Media(new File(musicFileString).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(musicFileMedia);
//        btnPlayPause.setOnAction(event -> {
//            if(btnPlayPause.isSelected()) {
//                mediaPlayer.play();
//            } else {
//                mediaPlayer.pause();
//            }
//        });

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

    private SongsDAO songsDAO = new SongsDAO();


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






package easv.g5tunes.gui.controllers;

import easv.g5tunes.MyTunes;
import easv.g5tunes.be.Songs;
import easv.g5tunes.bll.SongService;
import easv.g5tunes.exceptions.MyTuneExceptions;
import easv.g5tunes.gui.model.SongsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {

    private final SongsModel songsModel = new SongsModel();

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
    SongService songService = new SongService();

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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/easv/g5tunes/addNewSong.fxml"));
        Parent scene = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.setTitle("G5Tunes");
        stage.centerOnScreen();

        stage.show();
    }

    public void onClickSongsDelete(ActionEvent actionEvent) {
    }

    public void onClickClose(ActionEvent actionEvent) {
    }

    public void OnClickSongonPlaylistDelete(ActionEvent actionEvent) {
    }

    public void OnClickSongonPlaylistScrollDown(ActionEvent actionEvent) {
    }

    public void onClickPlayStop(ActionEvent actionEvent) {
    }

    public void onClickFastForward(ActionEvent actionEvent) {
    }

    public void OnClickSongonPlaylistScrollUp(ActionEvent actionEvent) {
    }

    public void onClickAddSongsToPlaylist(ActionEvent actionEvent) {
    }

    public void onClickRewind(ActionEvent actionEvent) {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lstViewSongs.setItems(songsModel.getSongs());

        try {
            songsModel.loadSongs();
        } catch (MyTuneExceptions e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    private void showAlertWindow(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.showAndWait();
    }


}


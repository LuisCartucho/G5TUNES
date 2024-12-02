package easv.g5tunes.gui;

import easv.g5tunes.be.Songs;
import easv.g5tunes.gui.model.SongsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {

    private final SongsModel songsModel = new SongsModel();


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

    public void onClickFilterSearch(ActionEvent actionEvent) {
    }

    public void onClickPlaylistsNew(ActionEvent actionEvent) {

    }

    public void onClickPlaylistsEdit(ActionEvent actionEvent) {
    }

    public void onClickPlaylistsDelete(ActionEvent actionEvent) {
    }

    public void onClickSongsNew(ActionEvent actionEvent) {
    }

    public void onClickSongsEdit(ActionEvent actionEvent) {
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
    }
}


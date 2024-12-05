package easv.g5tunes.gui.controllers;

import easv.g5tunes.be.Songs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

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

import java.io.File;

public class AddNewSongController {


    private Songs selectedSongs;

    public void setSongs(Songs songs) {
        selectedSongs = songs;
        titleTxtFl.setText(songs.getTitle());
        artistTxtFl.setText(songs.getArtist());
    }


    @FXML
    private TextField titleTxtFl;
    @FXML
    private TextField artistTxtFl;
    @FXML
    private TextField timeTxtFl;
    @FXML
    private TextField fileTxtFl;
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private MyTunesController mainController;

    public void setMainController(MyTunesController mainController) {
        this.mainController = mainController;
    }

    public void onBtnChooseAct(ActionEvent actionEvent) {

    }

    public void onBtnSaveAct(ActionEvent actionEvent) {
        String title = titleTxtFl.getText();
        String artist = artistTxtFl.getText();
//        selectedSongs.setTitle(titleTxtFl.getText());
//        selectedSongs.setArtist(artistTxtFl.getText());



        ((Stage) btnSave.getScene().getWindow()).close();

        if(!title.isEmpty() && !artist.isEmpty() ) {
            Songs newSong = new Songs(title, artist);

            if (mainController != null) {
                mainController.addSongToListView(newSong);
                mainController.deleteSongFromListView();
            }

            ((Stage) btnSave.getScene().getWindow()).close();
        } else {
            System.out.println("All field are required!!");
        }


    }

    public void onBtnCancel(ActionEvent actionEvent) {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}

package easv.g5tunes.gui.controllers;

import easv.g5tunes.be.Songs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javafx.stage.Stage;


import java.io.File;

public class AddNewSongController {



    private Songs selectedSongs;

    public void setSongs(Songs songs) {
        selectedSongs = songs;
        titleTxtFl.setText(songs.getTitle());
        artistTxtFl.setText(songs.getArtist());
        fileTxtFld.setText(songs.getFilePath());
    }

    private String filePath;

    @FXML
    private TextField titleTxtFl;
    @FXML
    private TextField artistTxtFl;
    @FXML
    private TextField timeTxtFld;
    @FXML
    private TextField fileTxtFld;
    @FXML
    private Button btnChooseFile;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private MyTunesController mainController;

    public void onBrowseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files","*.mp3","*.wav"),
                new FileChooser.ExtensionFilter("All Files","*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            fileTxtFld.setText(filePath);
        }
    }

    public void onChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            fileTxtFld.setText(filePath);
            System.out.println("File chosen: " + filePath);
            System.out.println("TextField content after choosing: " + fileTxtFld.getText());
        } else {
            System.out.println("No file chosen.");
        }
    }

    public void setMainController(MyTunesController mainController) {
        this.mainController = mainController;
    }

    public void onBtnSaveAct(ActionEvent actionEvent) {
        String title = titleTxtFl.getText();
        String artist = artistTxtFl.getText();
        String filePath = fileTxtFld.getText();

        System.out.println("Title: " + title);
        System.out.println("Artist: " + artist);
        System.out.println("File path from TextField: " + filePath);

        if(!title.isEmpty() && !artist.isEmpty() && filePath != null && !filePath.isEmpty() ) {
            Songs newSong = new Songs(title, artist, filePath);

            if (mainController != null) {
                mainController.addSongToListView(newSong);
                mainController.deleteSongFromListView();
            }

            System.out.println("File saved with path: " + filePath);

            ((Stage) btnSave.getScene().getWindow()).close();
        } else {
            System.out.println("All field are required!!");
        }

    }

    public void onBtnCancel(ActionEvent actionEvent) {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }


}

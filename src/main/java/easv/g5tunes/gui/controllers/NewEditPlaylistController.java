package easv.g5tunes.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class NewEditPlaylistController {
    @FXML
    private TextField txtFieldNewOrEditPlaylist;

    private MyTunesController mainController;

    public void setMainController(MyTunesController mainController) {
        this.mainController = mainController;
    }

    public void onClickSaveButton(ActionEvent actionEvent) {

        String playlistName = txtFieldNewOrEditPlaylist.getText();

        if (playlistName != null && !playlistName.trim().isEmpty()) {
            mainController.addPlaylistToListView(playlistName);

            ((javafx.stage.Stage) txtFieldNewOrEditPlaylist.getScene().getWindow()).close();
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid playlist name.");
            alert.showAndWait();
        }

    }

    public void onClickCancelButton(ActionEvent actionEvent) {
        ((javafx.stage.Stage) txtFieldNewOrEditPlaylist.getScene().getWindow()).close();
    }
}

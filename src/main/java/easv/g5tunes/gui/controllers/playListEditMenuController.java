package easv.g5tunes.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class playListEditMenuController {

    private MyTunesController mainController;
    private String originalPlaylistName;

    @FXML
    private TextField txtFld;
    @FXML
    private Button saveBtn;

    public void setMainController(MyTunesController mainController) {
        this.mainController = mainController;
    }

    public void setPlaylistName(String playlistName) {
        originalPlaylistName = playlistName;
        txtFld.setText(playlistName);
    }

    public void onSaveBtn(ActionEvent actionEvent) {

        String updatedPlaylistName = txtFld.getText();

        if (updatedPlaylistName != null && !updatedPlaylistName.trim().isEmpty()) {
            mainController.updatePlaylistName(originalPlaylistName, updatedPlaylistName);

            ((javafx.stage.Stage) txtFld.getScene().getWindow()).close();
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid name.");
            alert.showAndWait();
        }

    }
}

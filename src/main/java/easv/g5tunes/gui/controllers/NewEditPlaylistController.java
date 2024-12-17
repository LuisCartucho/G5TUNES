package easv.g5tunes.gui.controllers;

import easv.g5tunes.dal.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.*;

public class NewEditPlaylistController {
    @FXML
    private TextField txtFieldNewOrEditPlaylist;
    DBConnection dbc = new DBConnection();


    private MyTunesController mainController;

    public NewEditPlaylistController() throws SQLException {
    }

    public void setMainController(MyTunesController mainController) {
        this.mainController = mainController;
    }

    public void onClickSaveButton(ActionEvent actionEvent) {
        // Get the playlist name from the input field
        String playlistName = txtFieldNewOrEditPlaylist.getText().trim();

        if (playlistName == null || playlistName.isEmpty()) {
            showAlert("Error", "Playlist name cannot be empty!");
            return;
        }

        // Sanitize playlist name (optional for safety)
        String sanitizedPlaylistName = playlistName.replaceAll("[^a-zA-Z0-9_ ]", "_");

        // SQL query to insert the playlist name into the Playlists table
        String insertPlaylistSQL = "INSERT INTO Playlists (playlistName) VALUES (?)";

        try (Connection con = dbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(insertPlaylistSQL)) {

            // Set the playlist name parameter
            stmt.setString(1, sanitizedPlaylistName);

            // Execute the query
            stmt.executeUpdate();

            // Notify the user of success
            showAlert("Success", "Playlist '" + playlistName + "' added successfully!");

            // Add the playlist name to the ListView in the main UI
            if (mainController != null) {
                mainController.addPlaylistToListView(playlistName);
            }

            // Close the playlist creation window
            ((javafx.stage.Stage) txtFieldNewOrEditPlaylist.getScene().getWindow()).close();

        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert("Error", "Playlist '" + playlistName + "' already exists!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add playlist: " + e.getMessage());
        }
    }

    public void onClickCancelButton(ActionEvent actionEvent) {
        ((javafx.stage.Stage) txtFieldNewOrEditPlaylist.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: You can provide a header if needed
        alert.setContentText(message);
        alert.showAndWait();
    }
}

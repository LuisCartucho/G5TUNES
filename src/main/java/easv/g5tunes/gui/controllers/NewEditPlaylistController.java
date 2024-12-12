package easv.g5tunes.gui.controllers;

import easv.g5tunes.dal.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.Statement;

public class NewEditPlaylistController {
    @FXML
    private TextField txtFieldNewOrEditPlaylist;
    DBConnection dbc = new DBConnection();


    private MyTunesController mainController;

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

        // Sanitize playlist name (replace invalid characters with '_')
        String sanitizedPlaylistName = playlistName.replaceAll("[^a-zA-Z0-9_]", "_");

        // SQL query to create the playlist table
        String createTableSQL = String.format(
                "CREATE TABLE %s (" +
                        "id INT PRIMARY KEY IDENTITY(1,1), " +
                        "title NVARCHAR(255) NOT NULL, " +
                        "artist NVARCHAR(255) NOT NULL, " +
                        "filepath NVARCHAR(MAX) NOT NULL)",
                sanitizedPlaylistName);

        try (Connection con = dbc.getConnection();
             Statement stmt = con.createStatement()) {

            // Execute the SQL query
            stmt.execute(createTableSQL);

            // Notify the user of success
            showAlert("Success", "Playlist '" + playlistName + "' created successfully!");

            // Add the playlist name to the ListView in the main UI
            if (mainController != null) {
                mainController.addPlaylistToListView(playlistName);
            }

            // Close the playlist creation window
            ((javafx.stage.Stage) txtFieldNewOrEditPlaylist.getScene().getWindow()).close();

        } catch (Exception e) {
            showAlert("Error", "Failed to create playlist: " + e.getMessage());
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

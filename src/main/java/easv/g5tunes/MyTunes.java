package easv.g5tunes;

import easv.g5tunes.exceptions.MyTuneExceptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunes extends Application {
    @Override
    public void start(Stage stage) throws MyTuneExceptions {
        FXMLLoader fxmlLoader = new FXMLLoader(MyTunes.class.getResource("MyTunesMainView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new MyTuneExceptions(e);
        }
        stage.setTitle("GROUP 5 TUNES");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
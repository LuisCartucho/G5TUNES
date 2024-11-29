package easv.g5tunes.gui.model;

import easv.g5tunes.be.Songs;
import easv.g5tunes.dal.db.ConnectionManager;
import easv.g5tunes.exceptions.MyTuneExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongsModel {

        private final ConnectionManager connectionManager = new ConnectionManager();
        private static final ObservableList <Songs> songs = FXCollections.observableArrayList();

    public void loadSongs() throws MyTuneExceptions {
        songs.setAll(connectionManager.getAllSongs());
    }






}




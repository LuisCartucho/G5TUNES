package easv.g5tunes.gui.model;

import easv.g5tunes.be.Songs;
import easv.g5tunes.bll.SongsManager;
import easv.g5tunes.dal.db.SongsDAODB;
import easv.g5tunes.exceptions.MyTuneExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class SongsModel {


        private SongsManager songsManager;
        private SongsDAODB songsDAODB;

        public SongsModel() {
            songsManager = new SongsManager();
            try {
                songsDAODB = new SongsDAODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public ObservableList<Songs> getAllSongs() {
            return FXCollections.observableArrayList(songsDAODB.getAllSongs());
        }
    }







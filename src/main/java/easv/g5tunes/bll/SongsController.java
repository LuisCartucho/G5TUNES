package easv.g5tunes.bll;


import easv.g5tunes.be.Songs;
import easv.g5tunes.dal.ISongsDAO;
import easv.g5tunes.dal.db.SongsDAODB;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.util.List;

public class SongsController {
    private final ISongsDAO songsDAO = new SongsDAODB();

    // Get all users
    public List<Songs> getAllSongs() throws MyTuneExceptions {
        return songsDAO.getAllSongs();
    }
}








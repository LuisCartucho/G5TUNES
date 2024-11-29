package easv.g5tunes.dal.db;


import easv.g5tunes.be.Songs;
import easv.g5tunes.dal.ISongsDAO;
import easv.g5tunes.dal.SongsDAO;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.util.List;

public class SongsController {
    private final ISongsDAO songsDAO = new SongsDAODB();

    // Get all users
    public List<Songs> getTittle() throws MyTuneExceptions {
        return songsDAO.getAll();
    }
}









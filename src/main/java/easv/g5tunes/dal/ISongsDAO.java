package easv.g5tunes.dal;

import easv.g5tunes.be.Songs;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.sql.SQLException;
import java.util.List;

public interface ISongsDAO {
    List<Songs> getAll() throws MyTuneExceptions;

    Songs add(Songs songs) throws MyTuneExceptions;

    void delete(Songs songs) throws MyTuneExceptions;

    void update(Songs songs) throws MyTuneExceptions;

    Songs get(int userId) throws MyTuneExceptions;
}

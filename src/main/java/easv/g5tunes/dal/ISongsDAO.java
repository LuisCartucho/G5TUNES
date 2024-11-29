package easv.g5tunes.dal;

import easv.g5tunes.be.Songs;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.sql.SQLException;
import java.util.List;

public interface ISongsDAO {
    List<Songs> getAll() throws MyTuneExceptions;

}

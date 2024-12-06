package easv.g5tunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.g5tunes.be.Songs;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.nio.file.Path;
import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;


    public class SongsDAO implements ISongsDAO {

    private final String splitChar = ";";
    private final Path filePath;
    public SongsDAO() {
        filePath = Paths.get("songs.cvs");
    }

    @Override
    public List<Songs> getAll() throws MyTuneExceptions {
        List<Songs> songs = new ArrayList<>();
        if (Files.exists(filePath)) {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(filePath);
            } catch (IOException e) {
                throw new MyTuneExceptions(e);
            }
            for (String line : lines) {
                String[] parts = line.split(splitChar);
                if (parts.length == 3) {
                    try {
                        String title = parts[0].trim();
                        String artist = parts[1].trim();
                        String filePath = parts[2].trim();
                        songs.add(new Songs(title, artist, filePath));
                    } catch (NumberFormatException e) {
                        // log the error instead of printing it
                        Logger.getLogger(SongsDAO.class.getName()).log(Level.WARNING, "Invalid song ID format: " + parts[0], e);
                    }
                } else {
                    Logger.getLogger(SongsDAO.class.getName()).log(Level.WARNING, "Invalid line format: "+ line);
                }
            }
        }
        return songs;
    }

    @Override
    public Songs add(Songs songs) throws MyTuneExceptions {
        return null;
    }

    @Override
    public void delete(Songs songs) throws MyTuneExceptions {

    }

    @Override
    public void update(Songs songs) throws MyTuneExceptions {

    }

    private void clearAndSave(List<Songs> songs) throws MyTuneExceptions {
        List<String> lines = new ArrayList<>();
        for (Songs song : songs) {
            lines.add(song.getTitle() + splitChar + song.getArtist());
        }
        try{
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new MyTuneExceptions(e);
        }
    }




}










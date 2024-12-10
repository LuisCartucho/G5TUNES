package easv.g5tunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import easv.g5tunes.be.Songs;
import easv.g5tunes.exceptions.MyTuneExceptions;

import java.io.File;
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


    public class SongsDAO {//implements ISongsDAO {


        // Method to get songs from a folder
        public List<Songs> getSongsFromFolder(String folderPath) {
            List<Songs> songsList = new ArrayList<>();
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".mp3")) { // Filter MP3 files
                            songsList.add(new Songs(file.getName(), file.getAbsolutePath()));
                        }
                    }
                }
            }
            return songsList;
        }
    }















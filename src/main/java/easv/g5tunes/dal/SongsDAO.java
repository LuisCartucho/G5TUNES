package easv.g5tunes.dal;


import easv.g5tunes.be.Songs;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

    public class SongsDAO {


        // Method to get songs from a folder
        public List<Songs> getSongsFromFolder(String folderPath) {
            List<Songs> songsList = new ArrayList<>();
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".mp3")) { // Filter MP3 files
                            songsList.add(new Songs(file.getName(),"unknown", file.getAbsolutePath()));
                        }
                    }
                }
            }
            return songsList;
        }
    }















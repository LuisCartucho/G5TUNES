package easv.g5tunes.bll;

import easv.g5tunes.be.Songs;

import java.io.File;


public class SongService {

    Songs songs;

    public Songs extractSongMetadata(File file) {
        String title = file.getName().replaceFirst("[.][^.]+$", ""); // Simplified logic
        String artist = "Unknown"; // Placeholder
        return new Songs(title, artist);
    }

    public void addSong(Songs song) {
        // Save to database and update UI
    }
}
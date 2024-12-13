package easv.g5tunes.bll;

import easv.g5tunes.be.Songs;
import easv.g5tunes.gui.model.SongsModel;
import java.util.List;
import java.util.stream.Collectors;

public class FilterService {
    public List<Songs> filterSongs(List<Songs> songs, String title, String artist, String filePath) {
        return songs.stream()
                .filter(song -> (title == null || title.isEmpty() || song.getTitle().equalsIgnoreCase(title)))
                .filter(song -> (artist == null || artist.isEmpty() || song.getArtist().equalsIgnoreCase(artist)))
                .filter(song -> (filePath == null || filePath.isEmpty() || song.getFilePath().equalsIgnoreCase(filePath)))
                .collect(Collectors.toList());
    }
}

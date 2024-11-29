package easv.g5tunes.be;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
    private int id;
    private String playlistName;
    private List<Songs> songs;
    private int numberOfSongs;
    private int totalDuration;

    public PlayList(int id, String playlistName, int numberOfSongs, int totalDuration) {
        this.id = id;
        this.playlistName = playlistName;
        this.songs = new ArrayList<Songs>();
        this.numberOfSongs = numberOfSongs;
        this.totalDuration = totalDuration;
    }

    public void addSong(Songs song) {
        songs.add(song);
    }

    public void removeSong(Songs song) {
        songs.remove(song);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<Songs> getSongs() {
        return songs;
    }

    public void setSongs(List<Songs> songs) {
        this.songs = songs;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}

package easv.g5tunes.be;

import java.util.List;

public class Songs {

        private int id;
        private String title;
        private String artist;
        private int duration; // Duration in seconds

        public Songs(int id, String title, String artist, int duration) {
                this.id = id;
                this.title = title;
                this.artist = artist;
                this.duration = duration;
        }



        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getArtist() {
                return artist;
        }

        public void setArtist(String artist) {
                this.artist = artist;
        }

        public int getDuration() {
                return duration;
        }

        public void setDuration(int duration) {
                this.duration = duration;
        }


        @Override
        public String toString() {
                return "Songs{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", artist='" + artist + '\'' +
                        ", duration=" + duration +
                        '}';
        }

        public void add(List<Songs> songs) {
        }
}



package easv.g5tunes.be;

import java.util.List;

public class Songs {


        private String title;
        private String artist;
        private String filePath;
        private int duration; // Duration in seconds //Maybe we can add this later

//        public Songs(String title, String artist) {
//
//                this(-1, title, artist);
//        }

        public Songs(String title, String artist, String filePath) {

                this.title = title;
                this.artist = artist;
                this.filePath = filePath;
        }

        public Songs(String title, String artist){
                this.title = title;
                this.artist = artist;
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

        public String getFilePath() {
                return filePath;
        }

        public void setFilePath(String filePath) {
                this.filePath = filePath;
        }


        @Override
        public String toString() {
                return  title +' '+'-'+' ' + artist ;
        }


}



package easv.g5tunes.be;

import java.util.List;

public class Songs {

        private int id;
        private String title;
        private String artist;
        private int duration; // Duration in seconds //Maybe we can add this later

//        public Songs(String title, String artist) {
//
//                this(-1, title, artist);
//        }

        public Songs(int id, String title, String artist) {
                this.id = id;
                this.title = title;
                this.artist = artist;
        }

        public Songs(String title, String artist){
                this.title = title;
                this.artist = artist;
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
                return  title +' '+'-'+' ' + artist ;
        }


}



package easv.g5tunes.be;

public class Songs {


        private String title;
        private String artist;
        private String filePath;
        private int duration; // Duration in seconds //Maybe we can add this later

        public Songs(String title,String artist,String filePath) {

                this.title = title;
                this.artist = artist;
                this.filePath = filePath;
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

        public String getDisplayName() { // this method removes the .mp3 from the name of the musics , to display them on the next method
                if (title.endsWith(".mp3")) {
                        return title.replace(".mp3", "");
                }
                return title;
        }


        @Override
        public String toString() {
                return getDisplayName() ;
        }


}



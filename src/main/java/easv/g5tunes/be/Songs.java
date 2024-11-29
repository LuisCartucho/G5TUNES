package easv.g5tunes.be;


import java.util.List;

public class Songs {
        private int id;
        private String title;
        private String artist;
        //private int time;

        public Songs(int id , String title, String artist) {
                this.id = id;
                this.title = title;
                this.artist = artist;
                //this.time = time;
        }





        public String getTitle() {
                return title;
        }

        public int getId(){
                return id;
        }

        public String getArtist() {
                return artist;
        }

        /**public int getTime() {
                return time;
        }
*/
        @Override
        public String toString() {
                return title + " - " + artist;
        }

        public void add(List<Songs> songs) {
        }
}


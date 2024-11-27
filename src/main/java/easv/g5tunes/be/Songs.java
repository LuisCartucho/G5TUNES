package easv.g5tunes.be;

public class Songs {
        private int id;
        private String name;
        private String path;

        public Songs(int id, String name, String path) {
                this.id = id;
                this.name = name;
        }

        public int getId() {
        return id;
        }

        public String getPath(){
        return path;
        }

        public void setPath(String path){
                this.path = path;
        }

        public void setId(int id) {
        this.id = id;
        }

        public String getName() {
        return name;
        }

        public void setName(String username) {
        this.name = username;
        }

        @Override
        public String toString() {
                return id + ", " + name;
        }
}



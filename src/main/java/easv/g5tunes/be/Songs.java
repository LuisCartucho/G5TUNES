package easv.g5tunes.be;

public class Songs {

private int id;
private String name;

public Songs(int id, String name) {
        this.id = id;
        this.name = name;
}

public int getId() {
        return id;
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



package appmoviles.com.reto2.model.pokemon;

import java.io.Serializable;

public class Type implements Serializable {
    private String name;

    public Type(String name) {
        this.name = name;
    }

    public Type() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

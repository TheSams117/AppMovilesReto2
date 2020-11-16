package appmoviles.com.reto2.model.pokemon;

import java.io.Serializable;

public class PokemonType implements Serializable {
    private Type type;

    public PokemonType(Type type) {
        this.type = type;
    }

    public PokemonType() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

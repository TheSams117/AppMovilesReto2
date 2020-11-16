package appmoviles.com.reto2.model.pokemon;

import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {
    private String name;
    private PokemonSprites sprites;
    private List<PokemonStat> stats;
    private List<PokemonType> types;
    private long timestamp;

    public Pokemon() {
    }

    public Pokemon(String name, PokemonSprites sprites, List<PokemonStat> stats, List<PokemonType> types) {
        this.name = name;
        this.sprites = sprites;
        this.stats = stats;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PokemonSprites getSprites() {
        return sprites;
    }

    public void setSprites(PokemonSprites sprites) {
        this.sprites = sprites;
    }

    public List<PokemonStat> getStats() {
        return stats;
    }

    public void setStats(List<PokemonStat> stats) {
        this.stats = stats;
    }

    public List<PokemonType> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonType> types) {
        this.types = types;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

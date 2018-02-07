package nl.markvanderwal.dvdcocoon.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@DatabaseTable(tableName = "Movies")
public class Movie implements Cloneable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String label;

    @DatabaseField
    private String actors;

    @DatabaseField
    private String description;

    @DatabaseField(canBeNull = true, foreign = true)
    private Medium medium;

    private List<Genre> genres;

    public Movie() {
        genres = new ArrayList<>();
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

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Medium getMedium() {
        if (medium == null) {
            return Medium.NULL_MEDIUM;
        }
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    @Override
    public String toString() {
        return getName();
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Movie)) return false;

        Movie otherMovie = (Movie) other;
        return otherMovie.getId() == getId();
    }

    @Override
    public Movie clone() {
        Movie copy = new Movie();

        copy.setId(getId());
        copy.setName(getName());
        copy.setLabel(getLabel());
        copy.setDescription(getDescription());
        copy.setActors(getActors());
        copy.setMedium(getMedium());

        List<Genre> genres = getGenres();
        List<Genre> copiedGenres = new ArrayList<>(genres);
        Collections.copy(copiedGenres, genres);
        copy.setGenres(copiedGenres);

        return copy;
    }

    @Override
    public int hashCode() {
        return getId();
    }
}

package nl.markvanderwal.dvdcocoon.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;
import nl.markvanderwal.dvdcocoon.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@DatabaseTable(tableName = "Genres")
public class Genre implements IdValueType {

    public final static String GENRE_ID_FIELD_NAME = "id";

    @DatabaseField(generatedId = true, columnName = GENRE_ID_FIELD_NAME)
    private int id;

    @DatabaseField
    private String name;

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

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Genre)) return false;

        Genre other = (Genre) o;
        return other.getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}

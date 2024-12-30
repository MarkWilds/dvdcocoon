package nl.markvanderwal.dvdcocoon.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;
import nl.markvanderwal.dvdcocoon.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@DatabaseTable(tableName = "Mediums")
public class Medium implements IdValueType {

    public static final Medium NULL_MEDIUM = new Medium();

    public Medium() {
        this(0, "N.V.T");
    }

    public Medium(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @DatabaseField(generatedId = true, columnName = "_id")
    private int id;

    @DatabaseField(columnName = "MediumName")
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
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Medium)) return false;

        Medium other = (Medium)o;
        return other.getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}

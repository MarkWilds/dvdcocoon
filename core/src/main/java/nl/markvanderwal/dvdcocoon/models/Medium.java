package nl.markvanderwal.dvdcocoon.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@DatabaseTable(tableName = "Medium")
public class Medium {

    @DatabaseField(generatedId = true)
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
}
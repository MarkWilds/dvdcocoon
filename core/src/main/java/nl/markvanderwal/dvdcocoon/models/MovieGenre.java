package nl.markvanderwal.dvdcocoon.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@DatabaseTable(tableName = "MoviesGenres")
public class MovieGenre {
    public final static String MOVIE_ID_FIELD_NAME = "movie_id";
    public final static String GENRE_ID_FIELD_NAME = "genre_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, columnName = MOVIE_ID_FIELD_NAME)
    private Movie movie;

    @DatabaseField(foreign = true, columnName = GENRE_ID_FIELD_NAME)
    private Genre genre;

    public MovieGenre() {
    }
}

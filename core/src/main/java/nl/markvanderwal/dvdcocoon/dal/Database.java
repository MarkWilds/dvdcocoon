package nl.markvanderwal.dvdcocoon.dal;

import com.j256.ormlite.dao.*;
import nl.markvanderwal.dvdcocoon.models.*;

import javax.inject.*;
import java.sql.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public interface Database {
    void initialize() throws SQLException, ClassNotFoundException;

    Dao<Movie, Integer> getMovieRepository();
    Dao<Medium, Integer> getMediumRepository();
    Dao<Genre, Integer> getGenreRepository();
    Dao<MovieGenre, Integer> getMovieGenreRepository();
}

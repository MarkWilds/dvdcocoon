package nl.markvanderwal.dvdcocoon.dal;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.*;
import com.j256.ormlite.support.*;
import com.j256.ormlite.table.*;
import nl.markvanderwal.dvdcocoon.models.*;

import java.sql.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class DesktopDatabase implements Database {

    private static final String databaseConnectionString = "jdbc:sqlite:database.sqlite";
    private ConnectionSource connectionSource;

    private Dao<Movie, Integer> daoMovies;
    private Dao<Medium, Integer> daoMediums;
    private Dao<Genre, Integer> daoGenres;
    private Dao<MovieGenre, Integer> daoMoviesGenres;

    @Override
    public void initialize() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connectionSource = new JdbcConnectionSource(databaseConnectionString);

        daoMovies = DaoManager.createDao(connectionSource, Movie.class);
        daoMediums = DaoManager.createDao(connectionSource, Medium.class);
        daoGenres = DaoManager.createDao(connectionSource, Genre.class);
        daoMoviesGenres = DaoManager.createDao(connectionSource, MovieGenre.class);

        TableUtils.createTableIfNotExists(connectionSource, Movie.class);
        TableUtils.createTableIfNotExists(connectionSource, Medium.class);
        TableUtils.createTableIfNotExists(connectionSource, Genre.class);
        TableUtils.createTableIfNotExists(connectionSource, MovieGenre.class);
    }

    @Override
    public Dao<Movie, Integer> getMovieRepository() {
        return daoMovies;
    }

    @Override
    public Dao<Medium, Integer> getMediumRepository() {
        return daoMediums;
    }

    @Override
    public Dao<Genre, Integer> getGenreRepository() {
        return daoGenres;
    }

    @Override
    public Dao<MovieGenre, Integer> getMovieGenreRepository() {
        return daoMoviesGenres;
    }
}

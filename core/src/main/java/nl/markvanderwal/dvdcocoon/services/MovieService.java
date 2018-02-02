package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.models.*;

import javax.inject.*;
import java.sql.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@Singleton
public class MovieService {

    private Dao<Movie, Integer> movies;

    @Inject
    public MovieService(@Named("DesktopDatabase") Database database) {
        movies = database.getMovieRepository();
    }

    public void createMovie(Movie movie) {
        try {
            movies.create(movie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Movie> getMovies() {
        try {
            return movies.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Movie getMovieById(int id) {
        try {
            return movies.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getMovieByName(String name) {

    }

    public void updateMovie(Movie movie) {

    }

    public void deleteMovie(Movie movie) {

    }
}

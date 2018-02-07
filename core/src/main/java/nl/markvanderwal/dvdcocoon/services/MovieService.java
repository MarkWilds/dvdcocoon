package nl.markvanderwal.dvdcocoon.services;

import com.google.common.base.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.stmt.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import org.apache.logging.log4j.*;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MovieService extends ObservableService<Movie> {

    private static final Logger LOGGER = LogManager.getLogger(MovieService.class);

    private Dao<MovieGenre, Integer> movieGenreDao;
    private MediumService mediumService;
    private GenreService genreService;

    private PreparedQuery<Genre> genresForMovieQuery;
    private PreparedQuery<Movie> movieForGenresQuery;

    public MovieService(Database database, MediumService mediumService, GenreService genreService) {
        super(database);
        this.mediumService = mediumService;
        this.genreService = genreService;
        this.movieGenreDao = database.getMovieGenreRepository();
        createGenreForMoviesQuery();
        createMovieForGenresQuery();

        try {
            mediumService.fetch();
        } catch (ServiceException e) {
            LOGGER.error("Gefaald om de medium data op te halen!");
        }

        try {
            genreService.fetch();
        } catch (ServiceException e) {
            LOGGER.error("Gefaald om de genre data op te halen!");
        }
    }

    public boolean isMovieValid(Movie movie) {
        return !Strings.isNullOrEmpty(movie.getLabel()) &&
                !Strings.isNullOrEmpty(movie.getName());
    }

    @Override
    protected Dao<Movie, Integer> initializeDao(Database database) {
        return database.getMovieRepository();
    }

    @Override
    public void fetch() throws ServiceException {
        super.fetch();

        // lazy load our mediums and genres
        observableDataList.forEach(movie -> {
            try {
                Medium medium = movie.getMedium();
                medium = mediumService.getById(medium.getId());
                if (medium != null) {
                    movie.setMedium(medium);
                }

                List<Genre> genres = fetchGenresForMovie(movie);
                if (genres.size() > 0) {
                    movie.setGenres(genres);
                }
            } catch (SQLException e) {
                LOGGER.error("Kon niet de benodigde data van de database ophalen voor film: " + movie.getName());
            } catch (ServiceException e) {
                LOGGER.warn("Kon genre of medium niet vinden voor film:" + movie.getName());
            }
        });
    }

    @Override
    public void create(Movie movie) throws ServiceException {
        super.create(movie);

        movie.getGenres().forEach(genre -> {
            try {
                attachGenre(genre, movie);
            } catch (ServiceException e) {
                LOGGER.error(String.format("Gefaald om %s\ngenre: %s aan film: %s te koppelen", e.getMessage(), genre, movie));
            }
        });
    }

    @Override
    public void update(Movie movie) throws ServiceException {
        super.update(movie);

        try {
            List<Genre> newGenreList = new ArrayList<>(movie.getGenres());
            List<Genre> oldGenreList = fetchGenresForMovie(movie);

            // get intersection of genres
            List<Genre> intersection = newGenreList.stream()
                    .filter(oldGenreList::contains)
                    .collect(Collectors.toList());

            // remove removed genres to database
            oldGenreList.removeAll(intersection);
            oldGenreList.forEach(genre -> {
                LOGGER.debug(String.format("Verwijder genre: [%s] van film: %s", genre, movie));
                try {
                    detachGenre(genre, movie);
                } catch (ServiceException e) {
                    LOGGER.error(String.format("Kon genre: %s niet verwijderen van film: %s", genre, movie));
                }
            });

            // add added genres to database
            newGenreList.removeAll(intersection);
            newGenreList.forEach(genre -> {
                LOGGER.debug(String.format("Voeg genre: [%s] aan film: %s toe", genre, movie));
                try {
                    attachGenre(genre, movie);
                } catch (ServiceException e) {
                    LOGGER.error(String.format("Kon genre: %s niet toevoegen aan film: %s", genre, movie));
                }
            });
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(Movie movie) throws ServiceException {
        deleteAllAttachedGenreCoupling(movie);
        super.delete(movie);
    }

    private void attachGenre(Genre genre, Movie movie) throws ServiceException {
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setGenre(genre);
        movieGenre.setMovie(movie);

        try {
            movieGenreDao.create(movieGenre);
        } catch (SQLException e) {
            throw new ServiceException("Niet gelukt om een koppeling tussen film en genre te maken!");
        }
    }

    private void detachGenre(Genre genre, Movie movie) throws ServiceException {
        DeleteBuilder<MovieGenre, Integer> deleteBuilder = movieGenreDao.deleteBuilder();

        try {
            deleteBuilder.where().eq(MovieGenre.MOVIE_ID_FIELD_NAME, movie)
                    .and().eq(MovieGenre.GENRE_ID_FIELD_NAME, genre);
            deleteBuilder.delete();
        } catch (SQLException ex) {
            throw new ServiceException(String.format("Kon genre: %s niet loskoppelen van film: %s!", genre, movie));
        }
    }

    private void deleteAllAttachedGenreCoupling(Movie movie) throws ServiceException {
        DeleteBuilder<MovieGenre, Integer> deleteBuilder = movieGenreDao.deleteBuilder();

        try {
            deleteBuilder.where().eq(MovieGenre.MOVIE_ID_FIELD_NAME, movie);
            deleteBuilder.delete();
        } catch (SQLException ex) {
            throw new ServiceException(String.format("Kon genres niet loskoppelen van film: %s!", movie));
        }
    }

    private List<Genre> fetchGenresForMovie(Movie movie) throws SQLException {
        genresForMovieQuery.setArgumentHolderValue(0, movie);
        List<Genre> coupledGenres = new ArrayList<>();
        List<Genre> genres = genreService.getDao().query(genresForMovieQuery);

        genres.forEach(g -> {
            try {
                Genre genre = genreService.getById(g.getId());
                coupledGenres.add(genre);
            } catch (ServiceException e) {
                LOGGER.error(String.format("Kon genre: %s niet uit service halen voor film: %s", g, movie));
            }
        });

        return coupledGenres;
    }

    public List<Movie> fetchMoviesForGenre(Genre genre) throws SQLException {
        movieForGenresQuery.setArgumentHolderValue(0, genre);
        return dao.query(movieForGenresQuery);
    }

    private void createGenreForMoviesQuery() {
        try {
            QueryBuilder<MovieGenre, Integer> movieGenreQb = movieGenreDao.queryBuilder();
            movieGenreQb.selectColumns(MovieGenre.GENRE_ID_FIELD_NAME);

            SelectArg genreSelectArguments = new SelectArg();
            movieGenreQb.where().eq(MovieGenre.MOVIE_ID_FIELD_NAME, genreSelectArguments);

            QueryBuilder<Genre, Integer> genreQb = genreService.getDao().queryBuilder();
            genreQb.where().in(Genre.GENRE_ID_FIELD_NAME, movieGenreQb);
            genresForMovieQuery = genreQb.prepare();
        } catch (SQLException ex) {
            LOGGER.error("Could not create prepared query for finding genres for movies!");
        }
    }

    private void createMovieForGenresQuery() {
        try {
            QueryBuilder<MovieGenre, Integer> movieGenreQb = movieGenreDao.queryBuilder();
            movieGenreQb.selectColumns(MovieGenre.MOVIE_ID_FIELD_NAME);

            SelectArg genreSelectArguments = new SelectArg();
            movieGenreQb.where().eq(MovieGenre.GENRE_ID_FIELD_NAME, genreSelectArguments);

            QueryBuilder<Movie, Integer> movieQb = dao.queryBuilder();
            movieQb.where().in(Genre.GENRE_ID_FIELD_NAME, movieGenreQb);
            movieForGenresQuery = movieQb.prepare();
        } catch (SQLException ex) {
            LOGGER.error("Could not create prepared query for finding genres for movies!");
        }
    }
}

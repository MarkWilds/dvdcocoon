package nl.markvanderwal.dvdcocoon.services;

import com.google.common.base.*;
import com.j256.ormlite.dao.*;
import javafx.beans.*;
import javafx.collections.*;
import javafx.util.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import org.apache.logging.log4j.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MovieService extends ObservableService<Movie> {

    private static final Logger LOGGER = LogManager.getLogger(MovieService.class);

    private Dao<MovieGenre, Integer> movieGenreDao;
    private MediumService mediumService;
    private GenreService genreService;

    public MovieService(Database database, MediumService mediumService, GenreService genreService) {
        super(database);
        this.mediumService = mediumService;
        this.genreService = genreService;
        this.movieGenreDao = database.getMovieGenreRepository();

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

        // lazy load our mediums
        observableDataList.forEach(movie -> {
            Medium medium = movie.getMedium();
            try {
                medium = mediumService.getById(medium.getId());
                movie.setMedium(medium);
            } catch (ServiceException e) {
                LOGGER.warn("Could not find medium for movie " + movie.getName());
            }
        });
    }
}

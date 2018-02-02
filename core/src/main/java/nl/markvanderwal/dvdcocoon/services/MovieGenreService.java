package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.models.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public class MovieGenreService extends BaseService<MovieGenre, Integer> {

    @Override
    protected Dao<MovieGenre, Integer> initializeDao(Database database) {
        return database.getMovieGenreRepository();
    }
}

package nl.markvanderwal.dvdcocoon.dagger;

import dagger.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.services.*;

import javax.inject.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
@Module
public class ServiceModule {
    @Provides
    @Singleton
    public static MovieService providesMovieService(Database database, MediumService mediumService,
                                                    GenreService genreService) {
        return new MovieService(database, mediumService, genreService);
    }

    @Provides
    @Singleton
    public static MediumService providesMediumService(Database database) {
        return new MediumService(database);
    }

    @Provides
    @Singleton
    public static GenreService providesGenreService(Database database) {
        return new GenreService(database);
    }
}

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
    public static MovieService providesMovieService(Database database) {
        MovieService service = new MovieService();
        service.setDatabase(database);
        return service;
    }

    @Provides
    @Singleton
    public static MediumService providesMediumService(Database database) {
        MediumService service = new MediumService();
        service.setDatabase(database);
        return service;
    }

    @Provides
    @Singleton
    public static GenreService providesGenreService(Database database) {
        GenreService service = new GenreService();
        service.setDatabase(database);
        return service;
    }

    @Provides
    @Singleton
    public static MovieGenreService providesMovieGenreService(Database database) {
        MovieGenreService service = new MovieGenreService();
        service.setDatabase(database);
        return service;
    }
}

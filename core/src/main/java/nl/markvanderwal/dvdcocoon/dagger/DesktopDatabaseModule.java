package nl.markvanderwal.dvdcocoon.dagger;

import dagger.*;
import nl.markvanderwal.dvdcocoon.dal.*;

import javax.inject.*;
import java.sql.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@Module
public class DesktopDatabaseModule {
    @Provides
    @Singleton
    public static Database provideDatabase() {
        Database database = new DesktopDatabase();
        try {
            database.initialize();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return database;
    }
}

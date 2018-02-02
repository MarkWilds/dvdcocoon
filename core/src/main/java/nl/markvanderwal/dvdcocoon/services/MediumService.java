package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import javafx.collections.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.models.*;

import javax.inject.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public class MediumService extends BaseService<Medium, Integer> {
    @Override
    protected Dao<Medium, Integer> initializeDao(Database database) {
        return database.getMediumRepository();
    }
}

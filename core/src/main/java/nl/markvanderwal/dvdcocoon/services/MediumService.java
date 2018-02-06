package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public class MediumService extends ObservableService<Medium> {

    public MediumService(Database database) {
        super(database);
    }

    @Override
    protected Dao<Medium, Integer> initializeDao(Database database) {
        return database.getMediumRepository();
    }

    @Override
    public Medium getById(Integer id) throws ServiceException{
        if(id == 0) {
            return Medium.NULL_MEDIUM;
        }

        return super.getById(id);
    }
}

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

    private Medium cachedData;

    public MediumService(Database database) {
        super(database);
        cachedData = new Medium();
    }

    @Override
    protected Dao<Medium, Integer> initializeDao(Database database) {
        return database.getMediumRepository();
    }

    /**
     * Gets a specific data from the local storage by id.
     * The data needs to have override equals and hashcode for it to work.
     *
     * @param id the unique identifier for the data
     * @return returns the data if found in local storage
     * @throws ServiceException
     */
    public Medium getById(Integer id) throws ServiceException{
        if(id == 0) {
            return Medium.NULL_MEDIUM;
        }

        cachedData.setId(id);

        if (observableDataList.contains(cachedData)) {
            int index = observableDataList.indexOf(cachedData);
            return observableDataList.get(index);
        } else {
            throw new ServiceException("Kon de specifieke data niet vinden");
        }
    }
}

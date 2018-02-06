package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import javafx.collections.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;

import java.sql.*;
import java.util.*;

/**
 * Observable services keep their data in memory and need to call fetch before using any of its member methods.
 *
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public abstract class ObservableService<data> implements Service<data> {
    protected ObservableList<data> observableDataList;
    protected Dao<data, Integer> dao;

    private Medium cachedMedium;

    ObservableService(Database database) {
        dao = initializeDao(database);
        observableDataList = FXCollections.observableArrayList();
        ;
        cachedMedium = new Medium(0, "TROLL");
    }

    /**
     * Gets the concrete dao from the subclass
     *
     * @param database the database used to get the dao from
     * @return the correct dao
     */
    protected abstract Dao<data, Integer> initializeDao(Database database);

    /**
     * Binds the client to this data source
     *
     * @return the list of data
     */
    public ObservableList<data> bind() {
        return observableDataList;
    }

    /**
     * Fetch the data from remote
     *
     * @throws ServiceException
     */
    public void fetch() throws ServiceException {
        observableDataList.clear();
        observableDataList.addAll(getAll());
    }

    /**
     * Gets a specific data from the local storage by id.
     * The data needs to have override equals and hashcode for it to work.
     *
     * @param id the unique identifier for the data
     * @return returns the data if found in local storage
     * @throws ServiceException
     */
    public data getById(Integer id) throws ServiceException {
        cachedMedium.setId(id);

        if (observableDataList.contains(cachedMedium)) {
            int index = observableDataList.indexOf(cachedMedium);
            return observableDataList.get(index);
        } else {
            throw new ServiceException("Kon de specifieke data niet vinden");
        }
    }

    /**
     * Get all data from remote
     *
     * @return list of all data from the remote storage
     * @throws ServiceException
     */
    public List<data> getAll() throws ServiceException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new ServiceException("Kon de verzameling van data niet ophalen");
        }
    }

    /**
     * creates new data and stores it in the local and remote storage
     *
     * @param value the data to create
     * @throws SQLException
     */
    public void create(data value) throws ServiceException {
        try {
            dao.create(value);
            observableDataList.add(value);
        } catch (SQLException e) {
            throw new ServiceException("Kon de data niet opslaan");
        }
    }

    /**
     * deletes given value from the database
     *
     * @param value value to delete
     * @throws ServiceException
     */
    public void delete(data value) throws ServiceException {
        try {
            dao.delete(value);
            observableDataList.remove(value);
        } catch (SQLException ex) {
            throw new ServiceException("Data kon niet verwijderd worden");
        }
    }

    /**
     * Updates given value
     *
     * @param value value to update
     * @throws ServiceException
     */
    public void update(data value) throws ServiceException {
        try {
            dao.update(value);

            int index = observableDataList.indexOf(value);
            if (index <= 0) {
                throw new ServiceException("data is neit aanwezig!");
            }
            observableDataList.set(index, value);
        } catch (SQLException ex) {
            throw new ServiceException("Data kon niet bijgewerkt worden");
        }
    }
}

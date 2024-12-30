package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.Dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.markvanderwal.dvdcocoon.dal.Database;
import nl.markvanderwal.dvdcocoon.exceptions.ServiceException;

import java.sql.SQLException;
import java.util.List;

/**
 * Observable services keep their data in memory and need to call fetch before using any of its member methods.
 *
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public abstract class ObservableService<data> implements Service<data> {
    protected ObservableList<data> observableDataList;
    protected Dao<data, Integer> dao;

    ObservableService(Database database) {
        dao = initializeDao(database);
        observableDataList = FXCollections.observableArrayList();
    }

    public Dao<data, Integer> getDao() {
        return dao;
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
            if (index < 0) {
                throw new ServiceException("data is niet aanwezig!");
            }
            observableDataList.set(index, value);
        } catch (SQLException ex) {
            throw new ServiceException("Data kon niet bijgewerkt worden");
        }
    }
}

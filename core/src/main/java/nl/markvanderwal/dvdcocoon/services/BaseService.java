package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.util.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;

import javax.inject.*;
import javax.xml.ws.*;
import java.sql.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
@Singleton
public abstract class BaseService<data, id> {
    protected ObservableList<data> dataObservableList;
    protected Dao<data, id> dao;

    BaseService() {
        dataObservableList = FXCollections.observableArrayList();
    }

    @Inject
    public void setDatabase(Database database) {
        dao = initializeDao(database);
    }

    /**
     * Gets the concrete dao from the subclass
     *
     * @param database the database used to get the dao from
     * @return the correct dao
     */
    protected abstract Dao<data, id> initializeDao(Database database);

    /**
     * Binds the client to this data source
     *
     * @return the list of data
     */
    public ObservableList<data> bind() {
        return dataObservableList;
    }

    /**
     * Fetch the data from remote
     */
    public void fetch() throws ServiceException  {
        try {
            List<data> dataList = dao.queryForAll();
            dataObservableList.clear();
            dataObservableList.addAll(dataList);
        } catch (SQLException e) {
            throw new ServiceException("Kon geen data ophalen");
        }
    }

    /**
     * create new data value
     *
     * @param value the data to create
     * @throws SQLException
     */
    public void create(data value) throws ServiceException {
        try {
            dao.create(value);
            dataObservableList.add(value);
        } catch (SQLException e) {
            throw new ServiceException("Kon de waarde niet opslaan");
        }
    }

    /**
     * Gets the data by ids
     *
     * @param value the id for the data to find
     * @return the found data
     * @throws ServiceException
     */
    public data attach(data value) throws ServiceException {
        try {
            if(dataObservableList.contains(value)) {
                int index = dataObservableList.indexOf(value);
                return dataObservableList.get(index);
            }

            List<data> remote = dao.queryForMatching(value);
            if(remote.size() > 0) {
                dataObservableList.add(remote.get(0));
                return remote.get(0);
            }

            throw new ServiceException("Could not find value by id");
        } catch (SQLException ex) {
            throw new ServiceException("Kon de waarde niet vinden");
        }
    }

    /**
     * deletes given value from the database
     * @param value value to delete
     * @throws ServiceException
     */
    public void delete(data value) throws ServiceException {
        try {
            dao.delete(value);
            dataObservableList.remove(value);
        } catch (SQLException ex) {
            throw new ServiceException("Data kon niet verwijderd worden");
        }
    }

    /**
     * Updates given value
     * @param value value to update
     * @throws ServiceException
     */
    public void update(data value) throws ServiceException {
        try {
            dao.update(value);

            int index = dataObservableList.indexOf(value);
            dataObservableList.set(index, value);
        } catch (SQLException ex) {
            throw new ServiceException("Data kon niet bijgewerkt worden");
        }
    }
}

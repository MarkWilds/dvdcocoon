package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import javafx.collections.*;
import nl.markvanderwal.dvdcocoon.dal.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;

import javax.inject.*;
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
    public void setDao(Database database) {
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
            throw new ServiceException("Could not fetch data from remote");
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
            throw new ServiceException("Could not create resource");
        }
    }

    /**
     * Gets the data by ids
     *
     * @param identifier the id for the data to find
     * @return the found data
     * @throws ServiceException
     */
    public data getById(id identifier) throws ServiceException {
        try {
            return dao.queryForId(identifier);
        } catch (SQLException ex) {
            throw new ServiceException("Could not find resource by id");
        }
    }
}

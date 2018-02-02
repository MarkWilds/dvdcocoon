package nl.markvanderwal.dvdcocoon.services;

import com.j256.ormlite.dao.*;
import javafx.collections.*;
import nl.markvanderwal.dvdcocoon.dal.*;

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
    public void fetch() {
        try {
            List<data> dataList = dao.queryForAll();
            dataObservableList.clear();
            dataObservableList.addAll(dataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create new data value
     *
     * @param value the data to create
     * @throws SQLException
     */
    public void create(data value) throws SQLException {
        try {
            dao.create(value);
            dataObservableList.add(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the data
     *
     * @param identifier the id for the data to find
     * @return the found data
     * @throws SQLException
     */
    public data getById(id identifier) throws SQLException {
        return dao.queryForId(identifier);
    }
}

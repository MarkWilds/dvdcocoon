package nl.markvanderwal.dvdcocoon.services;

import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 4-2-2018
 */
public interface Service<T> {

    List<T> getAll() throws Exception;

    T getById(Integer id) throws Exception;

    void create(T value) throws Exception;

    void update(T value) throws Exception;

    void delete(T value) throws Exception;
}

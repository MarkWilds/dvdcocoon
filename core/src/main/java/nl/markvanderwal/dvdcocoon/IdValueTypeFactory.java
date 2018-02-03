package nl.markvanderwal.dvdcocoon;

import nl.markvanderwal.dvdcocoon.*;
import nl.markvanderwal.dvdcocoon.models.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
@FunctionalInterface
public interface IdValueTypeFactory {
    IdValueType createValue(int id, String name);

    static IdValueType create(int id, String name, Class<? extends IdValueType> clazz) {
        IdValueType value = null;
        try {
            value = clazz.newInstance();
            value.setId(id);
            value.setName(name);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return value;
    }
}

package nl.markvanderwal.dvdcocoon;

import nl.markvanderwal.dvdcocoon.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
@FunctionalInterface
public interface IdValueTypeFactory {
    IdValueType createValue(int id, String name);
}

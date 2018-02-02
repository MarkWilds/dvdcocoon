package nl.markvanderwal.dvdcocoon.exceptions;

import nl.markvanderwal.dvdcocoon.dagger.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
}

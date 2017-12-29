# DVDCocoon 1.0
DVDCocoon is a simple movie management application that allows for syncing
between master and slave applications.

The project consists of 3 maven modules:
* core
* master
* slave

### Core
This is the DVDCocoon main module and will consist of:
* models for data
* services for business logic
* repositories as data access objects

It's the defacto 3 layer architecture used in enterprise.

### Master
This module is a desktop view for DVDCocoon
and allows full control of the movie data.

### Slave
This module is an android view for DVDCocoon
and allows only read/view functionality for the movie data.
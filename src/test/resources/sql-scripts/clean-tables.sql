-- Desactiva restricciones para poder borrar en orden libre
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE ratings;
TRUNCATE TABLE books;
TRUNCATE TABLE users;

-- Reactiva las restricciones
SET REFERENTIAL_INTEGRITY TRUE;

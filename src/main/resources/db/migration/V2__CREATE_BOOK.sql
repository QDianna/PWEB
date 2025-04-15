SET search_path = project, pg_catalog;

CREATE TABLE book (
                      id uuid,
                      title TEXT NOT NULL,
                      author TEXT NOT NULL,
                      PRIMARY KEY (id)
);
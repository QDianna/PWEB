SET search_path = project, pg_catalog;

ALTER TABLE events
    ADD COLUMN start_date TIMESTAMP NOT NULL DEFAULT NOW(),
ADD COLUMN end_date TIMESTAMP NOT NULL DEFAULT NOW();

-- remove the old column cuz no longer needed
ALTER TABLE events DROP COLUMN event_date;

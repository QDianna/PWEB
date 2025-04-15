SET search_path = project, pg_catalog;

CREATE TABLE events (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title TEXT NOT NULL,
    event_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES project.users(id)
);

CREATE TABLE reminders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    content TEXT,
    reminder_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES project.users(id)
);

CREATE TABLE notes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title TEXT,
    content TEXT,
    FOREIGN KEY (user_id) REFERENCES project.users(id)
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    event_id UUID,
    reminder_id UUID,
    message TEXT,
    scheduled_time TIMESTAMP NOT NULL,
    is_sent BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (event_id) REFERENCES project.events(id),
    FOREIGN KEY (reminder_id) REFERENCES project.reminders(id)
);

-- Rename table from 'event' to 'events'
ALTER TABLE task RENAME TO tasks;
ALTER TABLE tasklist RENAME TO tasklists;

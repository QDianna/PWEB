SET search_path = project, pg_catalog;

CREATE TABLE tasklist
(
    id    UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title TEXT,
    FOREIGN KEY (user_id) REFERENCES project.users (id)
);

CREATE TABLE task (
    id UUID PRIMARY KEY,
    tasklist_id UUID NOT NULL,
    content TEXT,
    FOREIGN KEY (tasklist_id) REFERENCES project.tasklist(id)
);




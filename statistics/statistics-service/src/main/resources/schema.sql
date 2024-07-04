BEGIN;

DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, app VARCHAR(200) NOT NULL, uri VARCHAR(200) NOT NULL, ip VARCHAR(100) NOT NULL, timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL);

COMMIT;
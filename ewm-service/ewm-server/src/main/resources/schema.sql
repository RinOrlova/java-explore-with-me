BEGIN;

DROP TABLE IF EXISTS compilation_event_views CASCADE;
DROP TABLE IF EXISTS user_event_views CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS categories CASCADE;

CREATE TABLE IF NOT EXISTS users (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, name VARCHAR(250) NOT NULL, email VARCHAR(254) NOT NULL UNIQUE);
CREATE TABLE IF NOT EXISTS categories (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, cat_name VARCHAR(100) NOT NULL UNIQUE);
CREATE TABLE IF NOT EXISTS locations (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, lat DOUBLE PRECISION NOT NULL, lon DOUBLE PRECISION NOT NULL);
CREATE TABLE IF NOT EXISTS events (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, annotation VARCHAR(2000) NOT NULL, category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE, created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL, description VARCHAR(7000) NOT NULL, event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, initiator_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE, location_id BIGINT NOT NULL REFERENCES locations(id) ON DELETE CASCADE, paid BOOLEAN NOT NULL, participant_limit INT NOT NULL, published_on TIMESTAMP WITHOUT TIME ZONE, request_moderation BOOLEAN NOT NULL, status VARCHAR(10) NOT NULL, title VARCHAR(120) NOT NULL, views BIGINT NOT NULL);
CREATE TABLE IF NOT EXISTS requests (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE, user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE, status VARCHAR(10) NOT NULL, created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL);
CREATE TABLE IF NOT EXISTS compilations (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, pinned BOOLEAN NOT NULL, title VARCHAR(7000) NOT NULL);
CREATE TABLE IF NOT EXISTS compilation_event_views (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE, compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE);

ALTER TABLE locations ADD CONSTRAINT unique_lat_lon UNIQUE (lat, lon);
ALTER TABLE categories ADD CONSTRAINT unique_cat_name UNIQUE (cat_name);

COMMIT;
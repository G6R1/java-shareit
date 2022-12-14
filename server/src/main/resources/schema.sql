--DROP TABLE IF EXISTS ITEMS, USERS, REQUESTS, BOOKINGS, COMMENTS;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name    VARCHAR(255)                                        NOT NULL,
    email   VARCHAR(512)                                        NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    description  VARCHAR(1024)                                       NOT NULL,
    requestor_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    created      timestamp
);


CREATE TABLE IF NOT EXISTS items
(
    item_id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name        VARCHAR(255)                                        NOT NULL,
    description VARCHAR(1024)                                       NOT NULL,
    available   BOOLEAN,
    owner_id    BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    request_id  BIGINT REFERENCES requests (request_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    start_time timestamp,
    end_time   timestamp,
    item_id    BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    booker_id  BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status     VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    text       VARCHAR(512),
    item_id    BIGINT REFERENCES items (item_id) ON DELETE CASCADE,
    author_id  BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    created    timestamp
);

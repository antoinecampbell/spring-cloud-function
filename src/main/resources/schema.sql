CREATE TABLE IF NOT EXISTS item
(
  id          SERIAL      NOT NULL PRIMARY KEY,
  name        VARCHAR(50) NOT NULL,
  description VARCHAR(255)
);
CREATE TABLE a (
  a BOOLEAN,
  b INTEGER,
  c SMALLINT,
  d BIGINT,
  e DOUBLE,
  f DECIMAL,
  g VARCHAR(20),
  h DATE,
  i TIME,
  j TIMESTAMP
);

CREATE TABLE b (
  id   BIGINT,
  name VARCHAR(20),
  age  SMALLINT
);

INSERT INTO a VALUES (TRUE, 5, -2, 87, 5.6, 7.88, 'Hello', '2000-12-20', '12:20:50', '2000-12-20 12:20:50');
INSERT INTO a VALUES (FALSE, -57, 78, 52538, 2785.578, -58, 'GGG', '2013-01-01', '01:01:01', '2000-12-01 05:05:05');
INSERT INTO b VALUES (1, 'name', 7);
INSERT INTO b VALUES (5, 'fg', 71);
INSERT INTO b VALUES (10, '45h', 20);
INSERT INTO b VALUES (20, 'ng', 97);
INSERT INTO b VALUES (50, 're', 572);

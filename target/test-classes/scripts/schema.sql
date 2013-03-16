-- drop table if exists customer;

CREATE MEMORY TABLE customer
(
  nickname VARCHAR(45) NOT NULL,
  dateOfBirth DATE NOT NULL,
  balance DOUBLE NOT NULL,
  PRIMARY KEY (nickname)
);
-- create unique index nickname_UNIQUE on customer(nickname);
-- CREATE TABLE JBT_MEM
-- (
--   ID         VARCHAR(5),
--   PAN_NUMBER VARCHAR(10),
--   ADDRESS    VARCHAR(40),
--   CITY       VARCHAR(35),
--   STATE      VARCHAR(2),
--   PINCODE    INTEGER
-- );
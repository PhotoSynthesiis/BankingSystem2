drop table if exists customer;

CREATE MEMORY TABLE customer
(
  nickname VARCHAR(45) NOT NULL,
  dateOfBirth DATE NOT NULL,
  balance DOUBLE NOT NULL,
  PRIMARY KEY (nickname)
);

DROP TABLE IF EXISTS customer;

CREATE MEMORY TABLE customer
(
  nickname     VARCHAR(45) NOT NULL,
  dateOfBirth  DATE        NOT NULL,
  balance      DOUBLE      NOT NULL,
  emailAddress VARCHAR(45) NOT NULL,
  isPremium    TINYINT     NOT NULL,
  joinDate     DATE,
  PRIMARY KEY (nickname)
);


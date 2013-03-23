DROP TABLE IF EXISTS customer;

CREATE MEMORY TABLE customer
(
  nickname     VARCHAR(45) NOT NULL,
  dateOfBirth  DATE        NOT NULL,
  balance      DOUBLE      NOT NULL,
  emailAddress VARCHAR(45) NOT NULL,
  isPremium    TINYINT,
  PRIMARY KEY (nickname)
);


DROP TABLE IF EXISTS customerStatus;

CREATE MEMORY TABLE customerStatus
(
  nickname               VARCHAR(45) NOT NULL,
  emailToCustomerHasSent TINYINT,
  PRIMARY KEY (nickname)
)

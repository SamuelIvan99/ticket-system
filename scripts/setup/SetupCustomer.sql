USE dmai0919_1081509

CREATE TABLE Customer
(
    CustomerID INT IDENTITY(1, 1) NOT NULL,

    FirstName VARCHAR(25) NOT NULL,
    LastName VARCHAR(25) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Type VARCHAR(25) NULL DEFAULT 'SomeCustomer',
    CompanyName VARCHAR(50) NOT NULL,
    Phone VARCHAR(50),

    AddressID INT NOT NULL,

    CONSTRAINT PK_Customer PRIMARY KEY (CustomerID),
    CONSTRAINT FK_Customer_Address FOREIGN KEY (AddressID) REFERENCES Address(AddressID)
        ON UPDATE CASCADE
)
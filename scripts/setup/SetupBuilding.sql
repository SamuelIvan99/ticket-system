USE dmai0919_1081509
-- DepartmentID and foreign key to department added
-- primary key is DepartmentID + AddressID for this table
CREATE TABLE Building
(
    DepartmentID INT NOT NULL,
    AddressID INT NOT NULL,

    CONSTRAINT PK_Building PRIMARY KEY (DepartmentID, AddressID),
    CONSTRAINT FK_Department FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID),
    CONSTRAINT FK_Building_Address FOREIGN KEY (AddressID) REFERENCES Address(AddressID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
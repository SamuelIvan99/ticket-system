USE dmai0919_1081509

CREATE TABLE Employee
(
    EmployeeID INT IDENTITY(1, 1) NOT NULL,

    FirstName VARCHAR(25) NOT NULL,
    LastName VARCHAR(25) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Type VARCHAR(25) NULL DEFAULT 'SomeEmployee',
    Password VARCHAR(45) NOT NULL,
    Phone VARCHAR(50),

    DepartmentID INT NOT NULL,

    CONSTRAINT PK_Employee PRIMARY KEY (EmployeeID),
    CONSTRAINT FK_Employee_Department FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID)
)
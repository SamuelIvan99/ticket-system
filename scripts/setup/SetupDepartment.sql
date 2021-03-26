USE dmai0919_1081509

CREATE TABLE Department
(
    DepartmentID INT IDENTITY(1, 1) NOT NULL,

    Name VARCHAR(50) NOT NULL,

    CONSTRAINT PK_Department PRIMARY KEY (DepartmentID)
)
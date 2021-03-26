USE dmai0919_1081509

CREATE TABLE Ticket
(
    TicketID INT IDENTITY(1, 1) NOT NULL ,

    StartDate DATETIME NOT NULL DEFAULT GETDATE(),
    EndDate DATETIME NULL,
    ComplaintStatus VARCHAR(50) NOT NULL DEFAULT 'SomeStatus',
    Priority VARCHAR(25) NULL DEFAULT 'SomePriority',

    EmployeeID INT NULL,
    CustomerID INT NOT NULL,
	
	VersionNo INT DEFAULT 0,

    CONSTRAINT PK_Ticket PRIMARY KEY (TicketID),
    CONSTRAINT FK_Ticket_Customer FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    CONSTRAINT FK_Ticket_Employee FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
        ON DELETE CASCADE
)
USE dmai0919_1081509

CREATE TABLE Inquiry
(
    InquiryID INT IDENTITY(1, 1) NOT NULL,

    Title VARCHAR(50) NOT NULL,
    Category VARCHAR(50) NOT NULL,
    Description VARCHAR(250) NOT NULL,
    Timestamp DATETIME NOT NULL DEFAULT GETDATE(),

    DepartmentID INT NULL,
    TicketID INT NOT NULL,

    CONSTRAINT PK_Inquiry PRIMARY KEY (InquiryID),
    CONSTRAINT FK_Inquiry_Department FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID),
    CONSTRAINT FK_Inquiry_Ticket FOREIGN KEY (TicketID) REFERENCES Ticket(TicketID)
)
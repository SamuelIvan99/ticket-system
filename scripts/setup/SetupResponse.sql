USE dmai0919_1081509

CREATE TABLE Response
(
    ResponseID INT IDENTITY(1, 1)  NOT NULL,

    Title VARCHAR(50) NOT NULL,
    Description VARCHAR(250) NOT NULL,
    Timestamp DATETIME NOT NULL DEFAULT GETDATE(),

    InquiryID INT NOT NULL,
    EmployeeID INT NULL,

    CONSTRAINT PK_Response PRIMARY KEY (ResponseID),
    CONSTRAINT FK_Response_Inquiry FOREIGN KEY (InquiryID) REFERENCES Inquiry(InquiryID),
    CONSTRAINT FK_Response_Employee FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
)
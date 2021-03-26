USE dmai0919_1081509

SELECT T.TicketID, I.Title, C.Email AS Customer, E.Email AS Employee,
	T.Priority, T.ComplaintStatus AS Status
FROM Ticket AS T JOIN(
	SELECT T.TicketID AS UniqueID, max_date = MAX(I.Timestamp)
	FROM Ticket AS T JOIN Inquiry AS I ON T.TicketID = I.TicketID
	GROUP BY T.TicketID
) AS LatestInquiries ON T.TicketID = LatestInquiries.UniqueID
	JOIN Inquiry AS I ON I.InquiryID = LatestInquiries.UniqueID
	LEFT JOIN Customer AS C ON C.CustomerID = T.CustomerID
	LEFT JOIN Employee AS E ON E.EmployeeID = T.EmployeeID
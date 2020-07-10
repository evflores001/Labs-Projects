SET PAUSE OFF

SET LINESIZE 80

SET PAGESIZE 55

spool startrekselect.txt

SELECT *
FROM TelProd;

SELECT *
FROM ObserProd;

SELECT *
FROM Customers;

SELECT *
FROM WorkOrder;

SELECT *
FROM PurchaseOrder;

SELECT *
FROM Vendor;

SELECT *
FROM Products;

SELECT *
FROM WorkOrderParts;

SELECT *
FROM PhotoProd;

SELECT *
FROM MsrmProd;

SELECT *
FROM CustomerOrder;

SELECT *
FROM WorkCenter;

SELECT *
FROM Parts;

SELECT *
FROM Employee;

SELECT *
FROM purchasePartOrder;

spool off

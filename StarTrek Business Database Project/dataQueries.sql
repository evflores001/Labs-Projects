SET PAUSE OFF

SET LINESIZE 80

SET PAGESIZE 55

spool dataQueries.txt

-- 1. Work order schedule reporte
SELECT wo.workOrderNo "Work Order Number", co.custOrder "Customer Order Number", tp.productType "Type", co.OrderSpecs "Order Description", wo.startDate "Planned Start Date", wo.startDate "Actual Start Date",  wo.endDate "Planned End Date", wo.endDate "Actual End Date", co.productType "Customer Order Type"
	FROM WorkOrder wo, CustomerOrder co, TelProd tp
	ORDER BY co.custOrder, wo.workOrderNo;

-- 2. Shop floor control report
SELECT e.employeeNo "Employee SSN", e.lName "Employee Name", wc.workCentNo "Work Center Location", co.OrderSpecs "Order Description", wo.workOrderNo "Work Order Number", wo.startDate "Planned Start Date", wo.endDate "Planned End Date"
	FROM Employee e, WorkCenter wc, CustomerOrder co, WorkOrder wo
	ORDER BY wc.workCentNo, co.custOrder, wo.workOrderNo, e.employeeNo;

-- 3. List of customer orders
SELECT co.OrderSpecs "Order Description", co.custOrder "Customer Order Type", co.Address "Customer Address", c.CreditLim "Customer Credit Limit"
	FROM CustomerOrder co, Customers c
	ORDER BY co.lName, co.custOrder;

-- 4. Parts usage report
SELECT co.custOrder "Customer Order Type", p.quantity "Quantity Required", co.OrderSpecs "Work Order Description", wo.workOrderNo "Work Order Type", wo.endDate "Actual End Date", wo.startDate "Actual Start Date"
	FROM CustomerOrder co, Parts p, WorkOrder wo
	ORDER BY co.custOrder, wo.workOrderNo, p.partNo;

-- 5. Work order schedule analysis report
SELECT (wo.endDate - wo.startDate) "Planned Time of Work Orders", (wo.endDate - wo.startDate) "Actual Time of Work Orders", wo.workOrderNo "Work Order Type", co.OrderSpecs "Work Order Description", wo.startDate "Actual Start Date", wo.startDate "Planned Start Date", wo.endDate "Actual End Date", wo.endDate "Planned End Date"
	FROM WorkOrder wo, CustomerOrder co
	ORDER BY co.custOrder, wo.workOrderNo;

-- 6. Open purchase orders report
SELECT v.VendorName "Vendor Name", v.VendorNo "Vendor Number", po.purchaseDate "Purchase Date", p.partsReq "Desc Parts Ordered", p.dateRequired "Date Order Received", p.quantity "Quant Order Received"
	FROM Vendor v, PurchaseOrder po, Parts p
	ORDER BY po.purchaseNo, p.partNo;

-- 7. List of inventory suppliers sorted by vendor number
SELECT p.partsReq "Parts Description", v.VendorName, v.street ||','|| v.city ||','|| v.postcode "Vendor Address", po.Price "Vendor Price"
	FROM Parts p, Vendor v, PurchaseOrder po
	ORDER BY v.VendorNo, p.partNo;

-- 8. Inventory control report
SELECT p.partNo "Parts Number", p.partsReq "Parts", p.quantity "Quantity"
	FROM Parts p
	ORDER BY p.partNo, p.partsReq, p.quantity;

spool off

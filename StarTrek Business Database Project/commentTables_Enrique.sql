/*    Project Option: <Star Trek> 
Purpose: Comment tables  
Author: Enrique Flores 004538390 
*/
SET PAUSE OFF

SET LINESIZE 80

SET PAGESIZE 55

spool tableComments.txt
COMMENT ON TABLE ObserProd IS 'Observation Products of Star Trek';
COMMENT ON COLUMN ObserProd.productType IS 'Observation product type';
COMMENT ON COLUMN ObserProd.oTelephone IS 'Telephone Number';
COMMENT ON COLUMN ObserProd.oEmail IS 'Email address';

COMMENT ON TABLE CustomerOrder IS 'Unique Customer Order report';
COMMENT ON COLUMN CustomerOrder.custOrder IS 'Customer"s order';
COMMENT ON COLUMN CustomerOrder.fName IS 'First Name';
COMMENT ON COLUMN CustomerOrder.lName IS 'Last Name';
COMMENT ON COLUMN CustomerOrder.address IS 'Customers Address';
COMMENT ON COLUMN CustomerOrder.orderSpecs IS 'Order Specifications';
COMMENT ON COLUMN CustomerOrder.productType IS 'Type of Order';
COMMENT ON COLUMN CustomerOrder.custNo IS 'Customer Number';

COMMENT ON TABLE WorkCenter IS 'Work Center Details';
COMMENT ON COLUMN WorkCenter.workCentNo IS 'Unique Work Center Number';
COMMENT ON COLUMN WorkCenter.startDate IS 'Order Start Date';
COMMENT ON COLUMN WorkCenter.endDate IS 'Order End Date';

COMMENT ON TABLE Employee IS 'Employee Details';
COMMENT ON COLUMN Employee.employeeNo IS 'Unique Employee Number';
COMMENT ON COLUMN Employee.fName IS 'First Name';
COMMENT ON COLUMN Employee.lName IS 'Last Name';
COMMENT ON COLUMN Employee.workCentNo IS 'Work Center Employee Works At';

COMMENT ON TABLE PurchasePartOrder IS 'Parts Required For Purchase';
COMMENT ON COLUMN PurchasePartOrder.purchaseNo IS 'Unique Purchase Number';
COMMENT ON COLUMN PurchasePartOrder.partNo IS 'Unique Part Number';

COMMENT ON TABLE WorkOrderParts IS 'Parts Required For Work Orders';
COMMENT ON COLUMN WorkOrderParts.WorkOrderNo IS 'Unique Work Order Number';
COMMENT ON COLUMN WorkOrderParts.partNo IS 'Unique Part Number';

COMMENT ON TABLE TelProd IS 'Products of Startrek';
COMMENT ON COLUMN TelProd.productType IS 'Unique Product Line Number';
COMMENT ON COLUMN TelProd.telephone IS 'Telephone number';
COMMENT ON COLUMN TelProd.email IS 'Email address';

COMMENT ON TABLE Customers IS 'Customers of Startrek';
COMMENT ON COLUMN Customers.custNo IS 'Unique Customer Number';
COMMENT ON COLUMN Customers.Name IS 'Name';
COMMENT ON COLUMN Customers.street IS 'Street Address';
COMMENT ON COLUMN Customers.city IS 'City';
COMMENT on COLUMN Customers.postcode IS 'Postcode';
COMMENT on COLUMN Customers.CreditLim IS 'Customer Credit Limit';

COMMENT ON TABLE WorkOrder IS 'Work Orders of Startrek';
COMMENT ON COLUMN WorkOrder.WorkOrderNo IS 'Unique Work Order Number';
COMMENT ON COLUMN WorkOrder.PartReq IS 'Part Required';
COMMENT ON COLUMN WorkOrder.Labor IS 'Labor Required';
COMMENT ON COLUMN WorkOrder.startDate IS 'Start Date';
COMMENT ON COLUMN WorkOrder.endDate IS 'End Date';
COMMENT ON COLUMN WorkOrder.custOrder IS 'Unique Customer Order';

COMMENT ON TABLE Parts IS 'Parts of Startrek';
COMMENT ON COLUMN Parts.partNo IS 'Unique Part Number';
COMMENT ON COLUMN Parts.partsReq IS 'Parts Required';
COMMENT ON COLUMN Parts.dateRequired IS 'Date Required';
COMMENT ON COLUMN Parts.quantity IS 'Quantity';

COMMENT ON TABLE Products IS 'Products of Startrek';
COMMENT ON COLUMN Products.productType IS 'Unique Product Line Number';
COMMENT ON COLUMN Products.email IS 'Email address';
COMMENT ON COLUMN Products.telephone IS 'Telephone number';

COMMENT ON TABLE PhotoProd IS 'Photography products of Star Trek';
COMMENT ON COLUMN PhotoProd.productType IS 'PhotoProd product type';
COMMENT ON COLUMN PhotoProd.telephone IS 'Telephone Number';
COMMENT ON COLUMN PhotoProd.email IS 'Email address';

COMMENT ON TABLE MsrmProd IS 'Measurments of Star Trek';
COMMENT ON COLUMN MsrmProd.productType IS 'PhotoProd product type';
COMMENT ON COLUMN MsrmProd.telephone IS 'Telephone Number';
COMMENT ON COLUMN MsrmProd.email IS 'Email address';

COMMENT ON TABLE PurchaseOrder IS 'Purchase Order Info ';
COMMENT ON COLUMN PurchaseOrder.purchaseNo IS 'Unique Purchase Numbe';
COMMENT ON COLUMN PurchaseOrder.purchaseDate IS 'Purchase Date of Order';
COMMENT ON COLUMN PurchaseOrder.deliverylnst IS 'Delivery Instruction';
COMMENT ON COLUMN PurchaseOrder.vendorNo IS 'Vendor Number';
COMMENT ON COLUMN PurchaseOrder.quantity IS 'Quantity of Order';
COMMENT ON COLUMN PurchaseOrder.price IS 'Total Price of Order';

COMMENT ON TABLE Vendor IS 'Vendor Info';
COMMENT ON COLUMN Vendor.vendorNo IS 'Unique Vendor Number';
COMMENT ON COLUMN Vendor.vendorName IS 'Vendor name';
COMMENT ON COLUMN Vendor.street IS 'Street address';
COMMENT ON COLUMN Vendor.city IS 'city';
COMMENT ON COLUMN Vendor.postcode IS 'postcode';

spool off

spool descTables.txt

SET PAUSE OFF
COLUMN table_name FORMAT A20
COLUMN column_name FORMAT A20
COLUMN comments FORMAT A40 WRAPPED
SET LINESIZE 132


DESC TelProd;

DESC ObserProd;

DESC Customers;

DESC WorkOrder;

DESC PurchaseOrder;

DESC Vendor;

DESC Products;

DESC WorkOrderParts;

DESC PhotoProd;

DESC MsrmProd;

DESC CustomerOrder;

DESC WorkCenter;

DESC Parts;

DESC Employee;

DESC purchasePartOrder;

spool off


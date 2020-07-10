/*    Project Option: <Star Trek> 
Purpose: Insert data into tables  
Author: Enrique Flores 004538390 
*/
SET PAUSE OFF

SET LINESIZE 80

SET PAGESIZE 55

spool dataInserts.txt
SET CONSTRAINTS ALL DEFERRED;

INSERT INTO TelProd VALUES ('PH','9091234567','abcd@csusb.edu');


INSERT INTO Customers VALUES ('200','John Doe','2800 Observatory Ave','Los Angeles','90027','50000');
INSERT INTO Customers VALUES ('100','Jane Smith','35899 Canfield Rd','Warner Springs','92086','80000');

INSERT INTO WorkOrder VALUES ('AAA999','Bolts','25','26-DEC-16','06-FEB-17','2345');
INSERT INTO WorkOrder VALUES ('ARP401','Handles','22','14-FEB-17','14-MAR-17','43346');

INSERT INTO Parts VALUES ('20','Bolts','31-DEC-17','500');
INSERT INTO Parts VALUES ('50','Handles','31-DEC-17','250');

INSERT INTO Products VALUES ('MP');
INSERT INTO Products VALUES ('T');
INSERT INTO Products VALUES ('O');
INSERT INTO Products VALUES ('PH');

INSERT INTO OBSERPROD VALUES ('O','7608830716','telescope_comp@gmail.com');


INSERT INTO CUSTOMERORDER VALUES ('2345','John','Doe','2800 Observatory Ave Los Angeles, CA 90027 USA','We needed the largest lenses available, ASAP','200','T');
INSERT INTO CUSTOMERORDER VALUES ('43346','Jane','Smith','35899 Canfield Rd Warner Springs, CA 92086 USA','We are interested into purchasing some materials','100','O');

INSERT INTO WORKCENTER VALUES ('13','15-APR-19','28-MAY-19');
INSERT INTO WORKCENTER VALUES ('45','01-JAN-20','20-FEB-20');

INSERT INTO EMPLOYEE VALUES ('333445555','James','Borg','13');
INSERT INTO EMPLOYEE VALUES ('123121234','Ahmad','Jabbar','45');

INSERT INTO PURCHASEPARTORDER VALUES ('123456','20');
INSERT INTO PURCHASEPARTORDER VALUES ('654321','50');

INSERT INTO WORKORDERPARTS VALUES ('AAA999','20');
INSERT INTO WORKORDERPARTS VALUES ('ARP401','50');
INSERT INTO PhotoProd VALUES ('PH','88888999','nocool@gmail.com');


INSERT INTO MsrmProd VALUES ('MP','88880000','RILY@gmail.com');


INSERT INTO Vendor VALUES ('2','X','1415','London','70321');
INSERT INTO Vendor VALUES ('5','W','345','New York','1024');

INSERT INTO PurchaseOrder VALUES('654321','12-jun-19','drop off on building site','2','10','50');
INSERT INTO PurchaseOrder VALUES('123456','13-jun-19','drop off in mailbox','5','8','100');

SET CONSTRAINTS ALL IMMEDIATE;

spool off

/*    Project Option: <Star Trek> 
Purpose: Create tables  
Author: Enrique Flores 004538390 
*/

SET PAUSE OFF

SET LINESIZE 80

SET PAGESIZE 55

spool createTables.txt

DROP TABLE ObserProd CASCADE CONSTRAINTS;
CREATE TABLE ObserProd (
	productType VARCHAR(10) NOT NULL,
	oTelephone NUMBER(15),
	oEmail VARCHAR(25),
	CONSTRAINTS ObserProd_PK PRIMARY KEY (productType)
);

DROP TABLE CustomerOrder CASCADE CONSTRAINTS;
CREATE TABLE CustomerOrder (
	custOrder VARCHAR(10) NOT NULL, 
	fName VARCHAR(10) NOT NULL ,
	lName VARCHAR(10) NOT NULL,
	address VARCHAR(100) NOT NULL,
	orderSpecs VARCHAR(100) NOT NULL,
	custNo NUMBER(10) UNIQUE NOT NULL,
	productType VARCHAR(10) NOT NULL,
	CONSTRAINTS CustomerOrder_PK PRIMARY KEY (custOrder)
);

DROP TABLE WorkCenter CASCADE CONSTRAINTS;
CREATE TABLE WorkCenter (
	workCentNo VARCHAR(10) NOT NULL,
	startDate DATE NOT NULL,
	endDate DATE NOT NULL,
	CONSTRAINTS WorkCenter_PK PRIMARY KEY (workCentNo)
);

DROP TABLE Employee CASCADE CONSTRAINTS;
CREATE TABLE Employee (
	employeeNo NUMBER(10) NOT NULL,
	fName VARCHAR(10) NOT NULL,
	lName VARCHAR(10) NOT NULL,
	workCentNo VARCHAR(10) NOT NULL,
	CONSTRAINTS Employee_PK PRIMARY KEY (employeeNo)
);

DROP TABLE PurchasePartOrder CASCADE CONSTRAINTS;
CREATE TABLE purchasePartOrder (
	purchaseNo NUMBER(6) NOT NULL,
	partNo VARCHAR(15) NOT NULL,
	CONSTRAINTS PurchasePartOrder_PK PRIMARY KEY (purchaseNo, partNo)
);

DROP TABLE WorkOrderParts CASCADE CONSTRAINTS;
CREATE TABLE WorkOrderParts (
	WorkOrderNo VARCHAR(6) NOT NULL,
	partNo VARCHAR(15) NOT NULL,
	CONSTRAINTS WorkOrderParts_PK PRIMARY KEY (workOrderNo, partNo)
);

DROP TABLE TelProd CASCADE CONSTRAINTS;
CREATE TABLE TelProd (
	productType		VARCHAR(10)		NOT NULL,
	telephone		NUMBER(15),
	email			VARCHAR(25)		NOT NULL,
	CONSTRAINTS TelProd_productType_PK PRIMARY KEY (productType)
);

DROP TABLE Customers CASCADE CONSTRAINTS;
CREATE TABLE Customers (
	custNo			NUMBER(10)		NOT NULL,
	Name			VARCHAR(20)		NOT NULL,
	street			VARCHAR(20)		NOT NULL,
	city			VARCHAR(15)		NOT NULL,
	postcode		NUMBER(5)		NOT NULL,
	CreditLim		NUMBER(6)		NOT NULL,
	CONSTRAINTS Customers_custNo_PK PRIMARY KEY (custNo)
);

DROP TABLE WorkOrder CASCADE CONSTRAINTS;
CREATE TABLE WorkOrder (
	WorkOrderNo		VARCHAR(6)		NOT NULL,
	PartReq			VARCHAR(15)		NOT NULL,
	Labor			VARCHAR(15)		NOT NULL,
	startDate		DATE			NOT NULL,
	endDate			DATE			NOT NULL,
	custOrder		VARCHAR(10)		NOT NULL,
	CONSTRAINTS WorkOrder_WorkOrderNo_PK PRIMARY KEY (WorkOrderNo)
);

DROP TABLE Parts CASCADE CONSTRAINTS;
CREATE TABLE Parts (
	partNo			VARCHAR(6)		NOT NULL,
	partsReq		VARCHAR(15)		NOT NULL,
	dateRequired	DATE			NOT NULL,
	quantity		NUMBER(3)		NOT NULL,
	CONSTRAINTS Parts_partNo_PK PRIMARY KEY (partNo)
);

DROP TABLE Products CASCADE CONSTRAINTS;
CREATE TABLE Products (
	productType		VARCHAR(10)		NOT NULL,
	CONSTRAINTS Products_productType_PK PRIMARY KEY (productType)
);

DROP TABLE PhotoProd CASCADE CONSTRAINTS;
CREATE TABLE PhotoProd (
        productType VARCHAR(10) NOT NULL,
        telephone NUMBER(15)NOT NULL,
        email VARCHAR(25) NOT NULL,
        CONSTRAINTS PhotoProd_PK PRIMARY KEY (productType)
);
DROP TABLE MsrmProd CASCADE CONSTRAINTS;
CREATE TABLE MsrmProd (
        productType VARCHAR(10) NOT NULL,
        telephone NUMBER(15) NOT NULL,
        email VARCHAR(25) NOT NULL,
        CONSTRAINTS MsrmProd_PK PRIMARY KEY (productType)
);
DROP TABLE PurchaseOrder CASCADE CONSTRAINTS;
CREATE TABLE PurchaseOrder (
        purchaseNo NUMBER(6) NOT NULL,
        purchaseDate DATE NOT NULL,
        deliverylnst VARCHAR(25) NOT NULL,
        vendorNo VARCHAR(10) NOT NULL,
	quantity NUMBER(3) NOT NULL,
        price NUMBER(6) NOT NULL,
        CONSTRAINTS PurchaseOrder_PK PRIMARY KEY (purchaseNo)
);
DROP TABLE Vendor CASCADE CONSTRAINTS;
CREATE TABLE Vendor (
        vendorNo VARCHAR(10) NOT NULL,
        vendorName VARCHAR(10) NOT NULL,
        street VARCHAR(20) NOT NULL,
        city VARCHAR(15) NOT NULL,
        postcode NUMBER(5) NOT NULL,
        CONSTRAINTS Vendor_PK PRIMARY KEY (VendorNo)
);
spool off

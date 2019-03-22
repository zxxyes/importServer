create table KZASSETSINFOR
(
  assetsname          VARCHAR(500),
  id                  CHAR(50) not null,
  haveinvoice         VARCHAR(2),
  invoicecode         VARCHAR(1000),
  xh                  int,
  COUNT               Decimal(16,2),
  RECORDELIVERDATE    Date,
  UNITPRICE           Decimal(16,2),
  projectId           VARCHAR(500),
  assetsId            VARCHAR(500),	
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
create database if not exists accounting;

use accounting;

create table if not exists contractor
(
  contractorID         int auto_increment
    primary key,
  contractorType       varchar(20) not null,
  contractorName       varchar(20) not null,
  contractorPhone      varchar(10) not null,
  contractorRequisites varchar(25) not null,
  contractorAdress     varchar(30) not null,
  constraint contractor_contractorName_uindex
    unique (contractorName),
  constraint contractor_contractorPhone_uindex
    unique (contractorPhone),
  constraint contractor_contractorRequisites_uindex
    unique (contractorRequisites)
);

create table if not exists product
(
  productID    int auto_increment
    primary key,
  productName  varchar(20) not null,
  productGroup varchar(20) not null,
  productCost  double      not null,
  constraint product_productName_uindex
    unique (productName)
);

create table if not exists users
(
  userID         int auto_increment
    primary key,
  userEmail      varchar(40)   not null,
  userPassword   varchar(20)   not null,
  userSurname    varchar(20)   null,
  userName       varchar(20)   null,
  userPatronymic varchar(20)   null,
  userWorkplace  varchar(20)   null,
  userCity       varchar(20)   null,
  userPhone      varchar(20)   null,
  userIsAdmin    tinyint(1)    not null,
  userTax        int default 0 not null,
  userAdding     int default 0 not null,
  constraint userEmail
    unique (userEmail)
);

create table if not exists sale
(
  saleID       int auto_increment
    primary key,
  userID       int          not null,
  contractorID int          null,
  saleDate     timestamp(3) not null,
  constraint sale_contractor_contractorID_fk
    foreign key (contractorID) references contractor (contractorid)
      on update cascade,
  constraint sale_users_userID_fk
    foreign key (userID) references users (userid)
      on update cascade on delete cascade
);

create table if not exists saleContent
(
  saleID     int not null,
  productID  int null,
  saleAmount int not null,
  constraint saleContent_product_productID_fk
    foreign key (productID) references product (productid)
      on update cascade,
  constraint saleContent_sale_saleID_fk
    foreign key (saleID) references sale (saleid)
      on update cascade on delete cascade
);

create table if not exists supply
(
  supplyID     int auto_increment
    primary key,
  userID       int          not null,
  contractorID int          null,
  supplyDate   timestamp(3) not null,
  constraint supply_contractor_contractorID_fk
    foreign key (contractorID) references contractor (contractorid)
      on update cascade,
  constraint supply_users_userID_fk
    foreign key (userID) references users (userid)
      on update cascade on delete cascade
);

create table if not exists supplyContent
(
  supplyID     int not null,
  productID    int null,
  supplyAmount int not null,
  constraint supplyContent_product_productID_fk
    foreign key (productID) references product (productid)
      on update cascade,
  constraint supplyContent_supply_supplyID_fk
    foreign key (supplyID) references supply (supplyid)
      on update cascade on delete cascade
);



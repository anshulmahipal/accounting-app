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


INSERT INTO accounting.contractor (contractorID, contractorType, contractorName, contractorPhone, contractorRequisites, contractorAdress) VALUES (5, 'Клиент', 'ОАО "Белгазпромбанк"', '172895565', 'р/с 9005677812345', '220025 г.Минск');
INSERT INTO accounting.contractor (contractorID, contractorType, contractorName, contractorPhone, contractorRequisites, contractorAdress) VALUES (9, 'Клиент', 'ОАО "Звезды"', '179804565', 'р/с 9990008761232', '220021 г.Минск');
INSERT INTO accounting.contractor (contractorID, contractorType, contractorName, contractorPhone, contractorRequisites, contractorAdress) VALUES (11, 'Клиент', 'ИП "Иванчук"', '293142819', 'р/с 1231234564561', '220020 г.Минск');
INSERT INTO accounting.contractor (contractorID, contractorType, contractorName, contractorPhone, contractorRequisites, contractorAdress) VALUES (16, 'Поставщик', 'ОАО "Товары"', '293456789', 'р/с 1111111111111', '220025 г.Минск');
INSERT INTO accounting.contractor (contractorID, contractorType, contractorName, contractorPhone, contractorRequisites, contractorAdress) VALUES (17, 'Поставщик', 'УП "Сталь"', '293569080', 'р/с 2345672345671', '220020 г.Минск');

INSERT INTO accounting.product (productID, productName, productGroup, productCost) VALUES (24, 'Кабель Ethernet', 'Кабеля', 20);
INSERT INTO accounting.product (productID, productName, productGroup, productCost) VALUES (25, 'Удлиннитель 28', 'Удлинители', 25);
INSERT INTO accounting.product (productID, productName, productGroup, productCost) VALUES (26, 'Розетка парная 45мм', 'Розетки', 45);
INSERT INTO accounting.product (productID, productName, productGroup, productCost) VALUES (27, 'Аккумулятор Lion', 'Аккумуляторы', 120);

INSERT INTO accounting.users (userID, userEmail, userPassword, userSurname, userName, userPatronymic, userWorkplace, userCity, userPhone, userIsAdmin, userTax, userAdding) VALUES (8, 'novik@gmail.com', 'FBkGHRAQHxAAHBcW', 'Новик', 'Денис', '', '', 'Солигорск', '', 1, 20, 10);
INSERT INTO accounting.users (userID, userEmail, userPassword, userSurname, userName, userPatronymic, userWorkplace, userCity, userPhone, userIsAdmin, userTax, userAdding) VALUES (10, 'parkeston@gmail.com', 'EQ8TGh4IX1Y=', '', '', '', '', '', '', 0, 20, 10);
INSERT INTO accounting.users (userID, userEmail, userPassword, userSurname, userName, userPatronymic, userWorkplace, userCity, userPhone, userIsAdmin, userTax, userAdding) VALUES (11, 'maxim@gmail.com', 'Vl1VVgAPWwQEWhkbAQ==', 'Шинович', 'Максим', '', '', 'Новогрудок', '', 1, 20, 10);
INSERT INTO accounting.users (userID, userEmail, userPassword, userSurname, userName, userPatronymic, userWorkplace, userCity, userPhone, userIsAdmin, userTax, userAdding) VALUES (12, 'vitachi@gmail.com', 'FBkGHRAQDxQBCAQH', '', '', '', '', '', '', 0, 20, 10);
INSERT INTO accounting.users (userID, userEmail, userPassword, userSurname, userName, userPatronymic, userWorkplace, userCity, userPhone, userIsAdmin, userTax, userAdding) VALUES (13, 'andrew@mail.ru', 'FB8SHhUYHxYU', 'Базука', 'Андрей', '', '', '', '293459020', 0, 20, 10);

INSERT INTO accounting.supply (supplyID, userID, contractorID, supplyDate) VALUES (57, 8, 16, '2018-12-17 11:20:35.185');
INSERT INTO accounting.supply (supplyID, userID, contractorID, supplyDate) VALUES (58, 10, 16, '2018-12-17 08:40:51.015');
INSERT INTO accounting.supply (supplyID, userID, contractorID, supplyDate) VALUES (59, 12, 16, '2018-12-17 08:41:55.280');
INSERT INTO accounting.supply (supplyID, userID, contractorID, supplyDate) VALUES (60, 13, 16, '2018-12-17 08:42:15.326');

INSERT INTO accounting.supplyContent (supplyID, productID, supplyAmount) VALUES (57, 25, 20);
INSERT INTO accounting.supplyContent (supplyID, productID, supplyAmount) VALUES (57, 26, 10);
INSERT INTO accounting.supplyContent (supplyID, productID, supplyAmount) VALUES (58, 24, 5);
INSERT INTO accounting.supplyContent (supplyID, productID, supplyAmount) VALUES (58, 27, 10);
INSERT INTO accounting.supplyContent (supplyID, productID, supplyAmount) VALUES (60, 25, 5);
INSERT INTO accounting.supplyContent (supplyID, productID, supplyAmount) VALUES (60, 24, 15);

INSERT INTO accounting.sale (saleID, userID, contractorID, saleDate) VALUES (16, 8, 11, '2018-12-17 08:46:07.407');
INSERT INTO accounting.sale (saleID, userID, contractorID, saleDate) VALUES (17, 10, 9, '2018-12-17 08:46:22.763');
INSERT INTO accounting.sale (saleID, userID, contractorID, saleDate) VALUES (18, 12, 5, '2018-12-17 08:46:36.672');
INSERT INTO accounting.sale (saleID, userID, contractorID, saleDate) VALUES (19, 11, 11, '2018-12-17 08:47:00.705');

INSERT INTO accounting.saleContent (saleID, productID, saleAmount) VALUES (16, 24, 2);
INSERT INTO accounting.saleContent (saleID, productID, saleAmount) VALUES (16, 25, 5);
INSERT INTO accounting.saleContent (saleID, productID, saleAmount) VALUES (17, 27, 10);
INSERT INTO accounting.saleContent (saleID, productID, saleAmount) VALUES (18, 24, 20);
INSERT INTO accounting.saleContent (saleID, productID, saleAmount) VALUES (19, 25, 22);
INSERT INTO accounting.saleContent (saleID, productID, saleAmount) VALUES (19, 27, 25);
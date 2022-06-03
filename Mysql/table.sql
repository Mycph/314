use csit314;
CREATE TABLE Customer
(
    cusNum   int auto_increment,
    cusPw    varchar(50) not null,
    cusName  VARCHAR(50) not null,
    cusDOB   date not null,
    phoneNum VARCHAR(50) not null,
    vipStart date default '2022-04-02',
    vipEnd   date default '2022-04-03',
    email    VARCHAR(80) not null,
    gender   VARCHAR(10) not null,
    plateNum varchar(20) not null,
    vehicleModel varchar(80) not null,
    primary key (cusNum)
);
CREATE TABLE Professional
(
    proNum   int auto_increment,
    proPw    varchar(50) not null,
    proName  VARCHAR(50) not null,
    proDOB   date not null,
    phoneNum VARCHAR(50) not null,
    pLevel   FLOAT,
    balance  FLOAT,
    location varchar(100),
    email    VARCHAR(80) not null,
    gender   VARCHAR(10) not null,
    primary key (proNum)
);

CREATE TABLE orders
(
    orderid        int,
    orderStartDate DATE,
    vehiclePlate varchar(50),
    location       VARCHAR(80),
    issue          varchar(150),
    O_cusNum         int not null,
    O_proNum         int not null,
    orderEndDate   DATE,
    review         text,
    rating         FLOAT,
    payCardNum     varchar(50),
    payType        varchar(20),
    EstimatePayment     float,
    actualPayment       float,
    primary key orders (orderid),
    foreign key (O_cusNum) references Customer (cusNum),
    foreign key (O_proNum) references Professional (proNum)
);
CREATE TABLE cur_orders
(
    cur_orderid        int auto_increment,
    orderStartDate DATE,
    vehiclePlate varchar(50),
    EstimatePayment          FLOAT,
    c_location       VARCHAR(80),
    issue          varchar(150),
    O_cusNum         int not null,
    O_proNum         int,
    sstate          varchar(30),
    isVip           varchar(20),
    primary key cur_orders (cur_orderid),
    foreign key (O_cusNum) references Customer (cusNum)
);

create trigger updateBal after insert
    on orders
    for each row
    begin
        update Professional set balance =balance+ NEW.EstimatePayment*0.5 where proNum = NEW.O_proNum;
    end;

create trigger updateplevel after insert
    on orders
    for each row
    begin
        update Professional set pLevel =0.9*plevel+ 0.1*NEW.rating where proNum = NEW.O_proNum;
    end;

create trigger delCurrOrder after insert
    on orders
    for each row
    begin
        delete from cur_orders where cur_orderid = New.orderid;
    end;


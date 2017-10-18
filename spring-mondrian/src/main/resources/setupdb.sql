-- -------------------------------
-- To be invoked by setupdb.sh  --
-- -------------------------------

create table customers (
    customer_id int PRIMARY KEY,
    name varchar(50),
    state char(2),
    city varchar(30),
    age int,
    gender char(1)
);

create table salespeople (
    salesperson_id int PRIMARY KEY,
    name varchar(50)
);

-- The main fact table
create table sales (
    sale_id int IDENTITY PRIMARY KEY,

    customer_id int,
    salesperson_id int,

    product varchar(50),
    price decimal(7,2),

    pay_method varchar(20),

    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (salesperson_id) REFERENCES salespeople(salesperson_id),
    CHECK (pay_method='cash' OR pay_method='credit card' OR pay_method='bank transfer')
);


insert into customers (customer_id, name, state, city, age, gender)
values
    (1, 'Otávio Dias Castro', 'RN', 'Natal', 55, 'M')
    ;

insert into salespeople (salesperson_id, name)
values
    (1, 'Isabella Oliveira Cunha')
    ;

insert into sales (customer_id, salesperson_id, product, price, pay_method)
values
    (1, 1, 'Smart TV LED 40" Ultra HD 4K', 2349.90, 'credit card')
    ;


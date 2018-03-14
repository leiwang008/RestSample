#https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html
#The table's column name is converted into underscore format instead of camel format, be careful with it. 
#firstName, lastName, street, city
insert into customer(first_name, last_name, gender, street, city) values
		 ('Laura', 'Steel', 'Female', '429 Seventh Av.', 'Dallas'),
		 ('Mike', 'Steve', 'Male', '49 Saint Bld.', 'New York'),
		 ('Lilian', 'Fill', 'Female', '123 Sunrise Av.', 'Dallas'),
		 ('Laude', 'Pin', 'Male', '12 Core Av.', 'New York'),
		 ('Susanne', 'King', 'Female', '366 - 20th Ave.', 'Olten');

#name, price
insert into product(name, price) values 
					('Iron Iron', 5.4),
					('Baby Car Seat', 86.99);

#customerId, total
insert into invoice(customer_id, total) values 
					(1, 1002.65),
					(2, 635.28);

#invoiceId, productId, quantity, cost
insert into item(invoice_id, product_id, quantity, cost) values 
				(1, 1 , 5, 6.75),
				(1, 2 , 10, 96.89),
				(2, 1 , 12, 6.8),
				(2, 2 , 6, 92.28);
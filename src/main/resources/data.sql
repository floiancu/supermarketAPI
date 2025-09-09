INSERT into Items (id, name, price) VALUES ('259a8a6b-ddea-491e-85e9-a6ec4511ad9f', 'Apple', 10);
INSERT into Items (id, name, price) VALUES ('0fe10956-ed17-4ac9-a1de-ef55417c362d', 'Banana', 30);
INSERT into Offers(id, price, quantity, start_date, end_date) VALUES ('a4a1d2db-f633-4da7-a03e-08f044c024f8', 30, 2, '2025-09-01', '2025-09-07');
INSERT into Items (id, name, price, offer) VALUES ('fe7cabb7-c0f7-4f22-87ea-4c3a3b0585d8', 'Peach', 20, 'a4a1d2db-f633-4da7-a03e-08f044c024f8');
INSERT into Offers(id, price, quantity, start_date, end_date) VALUES ('b2276833-9c96-4417-aa9e-b8a28143b455', 40, 10, '2025-09-01', '2025-10-01');
INSERT into Items (id, name, price, offer) VALUES ('b2f0e565-de8c-4520-b0ca-9c3abf2bf6c1', 'Plum', 5, 'b2276833-9c96-4417-aa9e-b8a28143b455');

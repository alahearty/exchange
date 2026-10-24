CREATE TABLE "user"
(
    id SERIAL PRIMARY KEY,
    email        VARCHAR(100) UNIQUE NOT NULL,
    firstname    VARCHAR(100) NOT NULL,
    lastname     VARCHAR(100) NOT NULL,
    password     TEXT NOT NULL
);

CREATE TABLE category 
(
	id SERIAL PRIMARY KEY,
	name VARCHAR(200) UNIQUE NOT NULL
);

CREATE TABLE listing 
(
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT NOT NULL,
	condition VARCHAR(100) NOT NULL,
	quantity INT NOT NULL,
	imageurl TEXT NOT NULL,
	visible BOOLEAN,
	userid INT NOT NULL REFERENCES "user"(id),
	categoryid INT NOT NULL REFERENCES category(id)
);

CREATE TABLE interest 
(
	id SERIAL PRIMARY KEY,
	offerorquantity INT NOT NULL,
	offereequantity INT NOT NULL,
	offereestatus VARCHAR(100) NOT NULL,
	offerorlistingid INT NOT NULL REFERENCES listing(id),
	offereelistingid INT NOT NULL REFERENCES listing(id)
);

INSERT INTO "user" (email, firstname, lastname, password) VALUES('john@doe.com', 'John', 'Doe', '$2a$12$2f8GmIYik0ILlfXJcBTbnuth.ZbKk0cIs/dUYwA8niCpX5X.igFNy');
INSERT INTO "user" (email, firstname, lastname, password) VALUES('bill@gates.com', 'Bill', 'Gates', '$2a$12$F14u7IklMqDGMwyqfYIprOP4X1639ZtFhpkt45tqWWB6A9w5WB8CS');
INSERT INTO "user" (email, firstname, lastname, password) VALUES('steve@jobs.com', 'Steve', 'Jobs', '$2a$12$bGeVQSIGWeVmhTQZU/hF6uWyLevnOVmBm0NH3I7dSf0oFAUZ0gzpi');

INSERT INTO category (name) VALUES('Electronics');
INSERT INTO category (name) VALUES('Clothing');
INSERT INTO category (name) VALUES('Furniture');

INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('iPhone X', 'Brand: Apple, RAM:8GB, Model: iPhone X, Colour: Silver', 'new', 2, 'https://i5.walmartimages.com/seo/Restored-Apple-iPhone-X-256GB-Factory-Unlocked-Smartphone-Refurbished_9b5ec8b2-9665-463b-adc5-64829ba72da6.1b496e5a8fcee76fdad69bae12b54745.jpeg', 'true', 1, 1);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Top', 'Brand: Nike, Size: Large, Colour: Black', 'old', 2, 'https://dks.scene7.com/is/image/GolfGalaxy/21NIKWNKCRPTPXXXXTPS_Pacific_Blue?qlt=70&wid=600&fmt=pjpeg', 'true', 1, 2);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('IKEA Sofa', 'Brand: IKEA, Type: Sofa, Colour: Brown', 'new', 1, 'https://www.ikea.com/us/en/images/products/uppland-sofa-blekinge-white__0818564_pe774486_s5.jpg', 'true', 1, 3);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Console', 'Brand: Sony, Colour: gold, Model: play station 5', 'new', 1,'https://i5.walmartimages.com/seo/Sony-PlayStation-5-Video-Game-Console-Ps5-Disc-Console-New_396aa760-b2f6-4c06-8dc5-276184a85dc6.0d8c80439262394fc4edabb03056daa3.jpeg', 'true', 1, 1);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('IKEA Bookcase', 'Brand: IKEA, Type: Bookcase, Colour: white', 'new', 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSM_yaPcsL15cffeDQmgvfxYZBIRWsQ7iuqUw&s', 'true', 1, 3);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Shoes', 'Brand: Nike, Size:7, Colour: blue', 'new', 3, 'https://sothebys-com.brightspotcdn.com/dims4/default/07f69eb/2147483647/strip/true/crop/658x358+0+0/resize/684x372!/quality/90/?url=http%3A%2F%2Fsothebys-brightspot.s3.amazonaws.com%2Fdotcom%2F00%2F76%2F0703bab14d9db8e90b9594ffa9dc%2Fsothebys-md.brightspotcdn.jpg', 'true', 2, 2);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Jacket', 'Brand: Next, Size: Medium, Colour: blue', 'new', 3,'https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/f06ef6dd-d2f7-4dfc-b73f-373c68c444a3/sportswear-club-mens-puffer-jacket-1p1LL5.png', 'true', 2, 2);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('DFS Chair', 'Brand: DFS, Type: chair, Colour: white', 'new', 2, 'https://images.dfs.co.uk/i/dfs/sophia_1a_sophia_pearlcombination_view1?$dfs_v2_pdp_m$&fmt=auto', 'true', 2, 3);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Air Pods', 'Brand: Apple, Colour: white', 'new', 2, 'https://www.bengadgets.com.ng/wp-content/uploads/2021/12/MME73.jpg', 'true', 2, 1);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Samsung S20', 'BBrand: Samsung,RAM:6GB,Model:S20, Colour: green', 'new', 2, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzSoXjhqpCtKCZpeue_j26EZIYRSO3a1dkIg&s', 'true', 3, 1);
INSERT INTO listing (name, description, condition, quantity, imageUrl, visible, userId, categoryId) VALUES('Samsung S10', 'Brand:Samsung,RAM:3GB,Model:S10, Colour: black', 'new', 2, 'https://www-konga-com-res.cloudinary.com/w_400,f_auto,fl_lossy,dpr_3.0,q_auto/media/catalog/product/Z/W/163129_1675441157.jpg', 'true', 3, 1);
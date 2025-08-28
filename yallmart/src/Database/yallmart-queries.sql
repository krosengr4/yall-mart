# ---------------------------------------------------------------------- #
# Target DBMS:           MySQL                                           #
# Project name:          yallmart                               #
# ---------------------------------------------------------------------- #
DROP DATABASE IF EXISTS yallmart;

CREATE DATABASE IF NOT EXISTS yallmart;

USE yallmart;

-- Creating tables
CREATE TABLE users(
	user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    hashed_password VARCHAR(225) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE profiles(
	user_id INT NOT NULL,
	first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(200),
    address VARCHAR(200),
    city VARCHAR(50),
    state VARCHAR(50),
    zip VARCHAR(20),
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Like hats, pants, cowboy hats etc.    
CREATE TABLE categories (
    category_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    PRIMARY KEY (category_id)
);

CREATE TABLE products (
    product_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT NOT NULL,
    description TEXT,
    -- color VARCHAR(20), <---We could add this or not
    stock INT NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    date DATETIME NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip VARCHAR(20) NOT NULL,
    shipping_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_line_items (
    order_line_item_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    sales_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    -- discount DECIMAL(10, 2) NOT NULL DEFAULT 0, <-- Don't really want this for now, we can add later if we want
    PRIMARY KEY (order_line_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE shopping_cart (
	user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Creating categories
INSERT INTO categories (category_id, name, description) VALUES
(1, "Hats", "No Western look is complete without the perfect hat. Whether you're riding the range, dancing at the honky-tonk, or just soaking up the sun, our collection of cowboy hats and Western headwear has you covered."),
(2, "Boots", "Step into the spirit of the West with our collection of boots built for grit and style. Whether you’re working the ranch, heading to a country concert, or just looking to turn heads on Main Street, our cowboy and cowgirl boots deliver comfort, durability, and classic Western attitude."),
(3, "Shirts", "From the rodeo to the ranch and everywhere in between, our Western shirts bring timeless style and rugged comfort."),
(4, "Jeans", "Built for the long haul and the open trail, our Western jeans combine tough-as-nails durability with all-day comfort. Whether you're saddling up, hitting the dance floor, or putting in a hard day’s work, these jeans move with you and hold their shape."),
(5, "Jackets", "When the wind picks up and the nights get cool, our Western jackets have your back. From rugged denim and classic canvas to warm sherpa-lined and leather styles, these jackets are built to handle the elements with grit and style."),
(6, "Belts", "A true Western outfit isn’t complete without a belt that makes a statement. Whether you're keeping your jeans in place or showing off a bold buckle, our collection of Western belts combines rugged durability with standout style."),
(7, "Accessories", "It’s the little details that bring Western style to life. Our accessories collection adds the perfect finishing touch—think wild rags, bolo ties, belt buckles, bandanas, and more. Whether you're dressing up for a night out or gearing up for the trail, these Western-inspired pieces let your personality shine while staying true to rugged roots and timeless tradition.");

-- Inserting Products 
INSERT INTO products (name, price, category_id, description, stock) VALUES
-- Hats (1)
('Classic Cowboy Hat - Black Felt', 59.99, 1, 'A traditional black felt cowboy hat perfect for any ranch hand or rodeo night.', 25),
('Straw Cowboy Hat - Summer Breeze', 39.99, 1, 'Lightweight straw hat for hot summer days on the range.', 30),
('Leather Cowboy Hat - Outlaw Edition', 74.99, 1, 'Genuine leather hat with stitched brim and bandana accent.', 20),
('Wool Cowboy Hat - Midnight Shadow', 64.99, 1, 'Wool-blend cowboy hat with wide brim and dark charm.', 18),
('Vintage Rodeo Hat - Dust Storm', 89.99, 1, 'Distressed vintage look for rodeo nights and honky tonks.', 12),

-- Boots (2)
('Men''s Western Boots - Iron Rider', 129.99, 2, 'Rugged leather boots with steel toe and classic western design.', 15),
('Women''s Cowgirl Boots - Red Cactus', 114.99, 2, 'Bold red boots with cactus stitching detail.', 10),
('Kids Cowboy Boots - Little Wrangler', 59.99, 2, 'Comfortable and durable boots for young cowpokes.', 22),
('Classic Roper Boots - Brown Earth', 99.99, 2, 'Everyday boots for the ranch or town.', 17),
('Square Toe Cowboy Boots - Trail Dust', 119.99, 2, 'Modern square-toe design with traditional feel.', 14),

-- Shirts (3)
('Plaid Flannel Shirt - Western Nights', 34.99, 3, 'Soft flannel in red plaid for cool evenings.', 40),
('Denim Snap Shirt - Frontier Blue', 44.99, 3, 'Sturdy denim shirt with pearl snaps.', 35),
('Cowgirl Blouse - Wildflower', 38.99, 3, 'Lightweight blouse with floral western embroidery.', 28),
('Western Yoke Shirt - Desert Sky', 42.99, 3, 'Western-cut shirt with contrast yoke.', 31),
('Sleeveless Western Shirt - Rodeo Ready', 29.99, 3, 'Stay cool and stylish in the arena.', 27),

-- Jeans (4)
('Men''s Bootcut Jeans - Trailblazer', 54.99, 4, 'Classic bootcut jeans for riding and working.', 26),
('Women''s High-Waisted Jeans - Prairie Blue', 59.99, 4, 'Flattering and functional for the modern cowgirl.', 24),
('Distressed Cowboy Jeans - Rust Wash', 64.99, 4, 'Heavy-duty jeans with worn details.', 20),
('Slim Fit Western Jeans - Granite Gray', 49.99, 4, 'Stylish and versatile western jeans.', 18),
('Classic Indigo Jeans - Ranch Hand', 52.99, 4, 'Dark indigo jeans that never go out of style.', 22),

-- Jackets (5)
('Suede Fringe Jacket - Rodeo Queen', 89.99, 5, 'Eye-catching suede jacket with fringe detail.', 12),
('Denim Jacket - Western Cut', 74.99, 5, 'Heavyweight denim jacket with western seams.', 16),
('Shearling-Lined Jacket - Lone Star', 99.99, 5, 'Warm and rugged jacket for cold frontier nights.', 10),
('Leather Western Jacket - Black Gold', 119.99, 5, 'Sleek leather jacket with gold trim.', 8),
('Canvas Work Jacket - Dusty Trail', 84.99, 5, 'Tough canvas jacket with inner lining.', 14),

-- Belts (6)
('Tooled Leather Belt - Bronco Buckle', 49.99, 6, 'Hand-tooled leather belt with large bronco buckle.', 25),
('Studded Western Belt - Midnight Spurs', 44.99, 6, 'Black leather with silver studs and star buckle.', 21),
('Cowhide Belt - Ranger Style', 39.99, 6, 'Cowhide belt with vintage finish.', 19),
('Concho Belt - Silver & Turquoise', 59.99, 6, 'Decorative conchos with southwestern flair.', 15),
('Simple Leather Belt - Brown Oak', 34.99, 6, 'Everyday belt for jeans or slacks.', 30),

-- Accessories (7)
('Bandana - Red Paisley', 9.99, 7, 'Classic red bandana with paisley print.', 50),
('Western Bolo Tie - Eagle Emblem', 19.99, 7, 'Elegant bolo tie with eagle detail.', 35),
('Cowboy Boot Keychain - Mini Leather', 7.99, 7, 'Tiny boot keychain made of real leather.', 45),
('Leather Wrist Cuff - Wrangler Style', 14.99, 7, 'Adjustable leather cuff with western engraving.', 20),
('Horsehair Bracelet - Natural Twist', 12.99, 7, 'Braided horsehair bracelet with clasp.', 28);

SELECT * FROM products;
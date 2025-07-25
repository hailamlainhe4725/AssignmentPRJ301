USE [master]
GO
DROP DATABASE [PRJ301_ASSIGNMENT]
GO

CREATE DATABASE [PRJ301_ASSIGNMENT]
GO

USE [PRJ301_ASSIGNMENT]
GO



IF OBJECT_ID('dbo.tblUsers', 'U') IS NOT NULL
    DROP TABLE dbo.tblUsers;
GO

--  
CREATE TABLE dbo.tblUsers (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    userName NVARCHAR(50) NOT NULL UNIQUE,
    fullName NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20) UNIQUE,
    address NVARCHAR(255),
    role NVARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'CUSTOMER', 'SHIPPER')),
    wallet INT NOT NULL DEFAULT 0
);
GO


INSERT INTO dbo.tblUsers (userName, fullName, email, password, phone, address, role) VALUES
('admin',     'Administrator',   'admin@example.com',     '1',  '0123456789', '123 Admin St',   'ADMIN'),
('cust1',     'Nguyen Van A',    'cust1@example.com',     '1',   '0911223344', '456 Cust St',    'CUSTOMER'),
('shipper1',  'Pham Thi Binh',   'shipper1@example.com',  '1',   '0988776655', '789 Shipper Rd', 'SHIPPER');
GO

CREATE TABLE dbo.tblProducts (
    productId INT IDENTITY(1,1) PRIMARY KEY,
    productName NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    price FLOAT NOT NULL CHECK (price >= 0),
    imageUrl NVARCHAR(255),
    available BIT NOT NULL DEFAULT 1, -- true/false
    categoryId INT NOT NULL
);
GO


CREATE TABLE dbo.tblCategory (
    categoryId INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
	image NVARCHAR(100) NOT NULL
);
GO

INSERT INTO dbo.tblCategory(categoryName,description,image)
values
('Heo Quay','don thom ngon','/images/heoquay.jpg'),
('Vit Quay','dam vi','/image/vitquay.jpg'),
('Do An Kem','do ngan','/image/doankem.jpg'),
('other','banh mi va banh hoi','/image/other.jpg');
go

ALTER TABLE dbo.tblProducts
ADD CONSTRAINT FK_tblProducts_Category
FOREIGN KEY (categoryId)
REFERENCES dbo.tblCategory(categoryId);


INSERT INTO [dbo].[tblProducts]
           ([productName]
           ,[description]
           ,[price]
           ,[imageUrl]
           ,[available]
           ,[categoryId])
     VALUES
('Ba Roi', 'co nac va mo (100g)', 38000, '/image/baroi.jpg', 1, 1),
('Dui', 'nhieu nac (100g)', 38000, '/image/dui.jpg', 1, 1),
('Vit Quay Bac Kinh', 'ngot va beo (1 con)', 300000, '/image/vitbackinh.jpg', 1, 2),
('Vit Quay Da Gion Tu Vi', 'gion ngon thom(1 con)', 340000, '/image/vitdagion.jpg', 1, 2),
('Banh Mi', 'dac ruot', 5000, '/image/banhmi.jpg', 1, 3),
('Banh Hoi', 'cuon tron (1kg)', 20000, '/image/banhhoi.jpg', 1, 3),
('Banh Bao Chien', 'sieu ngon (3 cai)', 10000, '/image/banhbaochien.jpg', 1, 3),
('Du Chua', 'chua nhe 250g', 10000, '/image/duachua.jpg', 1, 3),
('Banh Mi Heo Quay', 'thom va gion (1 o)', 20000, '/image/banhmiheoquay.jpg', 1, 4),
('Banh Hoi Heo Quay', '(1 hop)', 25000, '/image/banhhoiheoquay.jpg', 1, 4);
GO


CREATE TABLE tblCarts (
    cartId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NOT NULL,
    createdAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (userId) REFERENCES tblUsers(userId)
);

CREATE TABLE tblCartItems (
    cartItemId INT IDENTITY(1,1) PRIMARY KEY,
    cartId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL,
	note NVARCHAR(500),
    FOREIGN KEY (cartId) REFERENCES tblCarts(cartId),
    FOREIGN KEY (productId) REFERENCES tblProducts(productId)
);
CREATE TABLE tblInventory (
	tuTang INT IDENTITY(1,1) PRIMARY KEY,
    inventoryId INT NOT NULL,
    productId INT NOT NULL,
    quantityAvailable INT NOT NULL,

    FOREIGN KEY (productId) REFERENCES tblProducts(productId)
);


INSERT INTO tblInventory (inventoryId, productId, quantityAvailable)
VALUES
(1, 5, 2),
(1, 6, 9),
(1, 7, 7),
(1, 8, 15),
(1, 9, 1),
(1, 10, 3),
(1, 11, 4),
(1, 12, 6),
(1, 13, 1),
(1, 14, 2);

CREATE TABLE tblPayment (
    so INT IDENTITY(1,1) PRIMARY KEY,
    phone VARCHAR(20),
    money INT,
    ma VARCHAR(20),
    CONSTRAINT FK_Payment_User FOREIGN KEY (phone) REFERENCES tblUsers(phone)
);

CREATE TABLE tblOrder (
    orderId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NOT NULL,
    totalAmount FLOAT NOT NULL CHECK (totalAmount >= 0),
    orderDate DATETIME DEFAULT GETDATE(),
    status NVARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'DELIVERED', 'CANCELED')),
    shippingAddress NVARCHAR(255),
    
    FOREIGN KEY (userId) REFERENCES tblUsers(userId)
);

CREATE TABLE tblOrderItems (
    orderItemId INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unitPrice FLOAT NOT NULL CHECK (unitPrice >= 0),

    FOREIGN KEY (orderId) REFERENCES tblOrder(orderId),
    FOREIGN KEY (productId) REFERENCES tblProducts(productId)
);
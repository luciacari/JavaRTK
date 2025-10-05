-- Создание таблицы товаров (Названия книг в книжном интернет-магазине)
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    category VARCHAR(100) NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE product IS 'Таблица книг';
COMMENT ON COLUMN product.id IS 'Идентификатор книги';
COMMENT ON COLUMN product.description IS 'Описание книги';
COMMENT ON COLUMN product.price IS 'Стоимость книги';
COMMENT ON COLUMN product.quantity IS 'Количество товара на складе';
COMMENT ON COLUMN product.category IS 'Жанр книги';
COMMENT ON COLUMN product.date_created IS 'Дата создания записи';

-- Создание таблицы клиентов (покупателей книг)
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE customer IS 'Таблица клиентов книжного магазина';
COMMENT ON COLUMN customer.id IS 'Идентификатор клиента';
COMMENT ON COLUMN customer.first_name IS 'Имя клиента';
COMMENT ON COLUMN customer.last_name IS 'Фамилия клиента';
COMMENT ON COLUMN customer.phone IS 'Телефон клиента';
COMMENT ON COLUMN customer.email IS 'Email клиента';
COMMENT ON COLUMN customer.date_created IS 'Дата создания записи';


-- Создание таблицы статусов заказов
CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON TABLE order_status IS 'Справочник статусов заказов';
COMMENT ON COLUMN order_status.id IS 'Идентификатор статуса';
COMMENT ON COLUMN order_status.name IS 'Наименование статуса заказа';

-- Заполнение справочника статусов
INSERT INTO order_status (name) VALUES
    ('Новый'),
    ('Оплачен'),
    ('Отправлен'),
    ('Доставлен'),
    ('Завершен'),
    ('Отменен')
ON CONFLICT (name) DO NOTHING;

-- Создание таблицы заказов
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES product(id),
    customer_id INTEGER NOT NULL REFERENCES customer(id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    status_id INTEGER NOT NULL REFERENCES order_status(id),
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0)
);

COMMENT ON TABLE orders IS 'Таблица заказов книжного магазина';
COMMENT ON COLUMN orders.id IS 'Идентификатор заказа';
COMMENT ON COLUMN orders.product_id IS 'Ссылка на книгу';
COMMENT ON COLUMN orders.customer_id IS 'Ссылка на клиента';
COMMENT ON COLUMN orders.order_date IS 'Дата заказа';
COMMENT ON COLUMN orders.quantity IS 'Количество товара в заказе';
COMMENT ON COLUMN orders.status_id IS 'Статус заказа';
COMMENT ON COLUMN orders.total_amount IS 'Общая сумма заказа';

-- Создание индексов
CREATE INDEX IF NOT EXISTS idx_orders_product_id ON orders(product_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_order_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_orders_status_id ON orders(status_id);
CREATE INDEX IF NOT EXISTS idx_product_category ON product(category);

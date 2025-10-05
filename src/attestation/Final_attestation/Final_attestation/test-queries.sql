-- 1. Запросы на чтение
-- 1.1. Запрос для проверки данных в таблице (без условий)
SELECT *
FROM customer c;

-- 1.2. Топ-3 самых популярных товара по количеству проданных единиц
SELECT
    p.id,
    p.description,
    p.category,
    SUM(o.quantity) AS total_items_sold
FROM product p
JOIN orders o ON p.id = o.product_id
JOIN order_status os ON o.status_id = os.id
WHERE
    os.name != 'Отменен'
GROUP BY p.id, p.description, p.category
ORDER BY total_items_sold desc, p.id
LIMIT 3;

-- 1.3. Список всех заказов за последние 7 дней с именем покупателя и описанием товара
SELECT
    o.id AS order_id,
    o.order_date,
    c.first_name || ' ' || c.last_name AS customer_name,
    p.description AS book_title,
    o.quantity,
    o.total_amount,
    os.name AS order_status
FROM
    orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
JOIN order_status os ON o.status_id = os.id
WHERE
    o.order_date >= CURRENT_DATE - INTERVAL '7 days'
ORDER BY
    o.order_date DESC, o.id;


-- 1.4. Общая сумма выручки, средняя цена заказа
SELECT
    SUM(total_amount) AS total_amount,
    AVG(total_amount) AS average_order_amount,
    COUNT(id) AS total_orders
FROM orders;

-- 1.5. Клиенты, сделавшие более одного заказа
SELECT
    c.first_name || ' ' || c.last_name AS customer_name,
    c.email,
    COUNT(o.id) AS orders_count
FROM
    customer c
JOIN orders o ON c.id = o.customer_id
GROUP BY
    c.first_name || ' ' || c.last_name, c.email
HAVING
    COUNT(o.id) > 1
ORDER BY
    orders_count DESC;

-- 1.6. Покупатели с наибольшей суммой покупок,
--      сортировка по убыванию суммы покупок покупателя.
--      Покупатели без покупок выведены в конце выборки
SELECT
    c.id,
    c.first_name || ' ' || c.last_name AS customer_name,
    c.email,
    COUNT(o.id) AS total_orders,
    SUM(o.total_amount) AS total_spent
FROM customer c
LEFT JOIN orders o ON c.id = o.customer_id
GROUP BY c.id, c.first_name, c.last_name, c.email
ORDER BY total_spent DESC NULLS LAST;

-- 1.7. Клиенты без заказов
SELECT * FROM customer
WHERE id NOT IN (
    SELECT DISTINCT customer_id
    FROM orders
);
-------------------------------------------------------------------
-- 2. Запросы на изменение

-- 2.1 Обновление цены товара по ID
UPDATE product
SET price = 355.00
WHERE id = 57;

-- 2.2 Обновление телефона покупателя
UPDATE customer
SET  phone  = '+7 (905) 234-56-77'
WHERE id = 2;

-- 2.3 Обновление количества на складе при покупке
UPDATE product
SET quantity = quantity - 1
WHERE description LIKE '%Харуки Мураками - Норвежский лес%';

----------------------------------------------------------------
-- 3. Запросы на удаление

-- 3.1 Удаление клиентов без заказов
DELETE FROM customer
WHERE id NOT IN (
    SELECT DISTINCT customer_id
    FROM orders
);

-- 3.2 Удаление конкретной книги по автору
DELETE FROM product
WHERE id = 10;


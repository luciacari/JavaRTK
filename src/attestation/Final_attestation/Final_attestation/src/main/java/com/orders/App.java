package com.orders;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import org.flywaydb.core.Flyway;

public class App {
    private Connection connection;
    private Properties props;

    public static void main(String[] args) {
        App app = new App();
        try {
            app.run();
        } catch (Exception e) {
            System.err.println("Ошибка приложения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() throws SQLException, IOException {
        // Загрузка конфигурации БД из файла application.properties
        loadProperties();

        //Запуск миграции данных с помощью Flyway
        runFlywayMigrations();

        // Подключение к БД, используя данные из конфигурационного файла
        connectToDatabase();

        //Демонстрация CRUD операций
        demonstrateCRUDOperations();

        //Выполнение тестовых запросов
        executeTestQueries();

        //Закрытие подключения к БД
        closeConnection();
    }

    // Загрузка конфигурации БД из файла application.properties
    private void loadProperties() throws IOException {
        props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IOException("Файл application.properties не найден в classpath");
            }
            props.load(in);
        }
        System.out.println("Настройки из конфигурационного файла успешно загружены.");
    }
   //Запуск миграции данных с помощью Flyway
    private void runFlywayMigrations() {
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        System.out.println("Запуск миграции с помощью Flyway...");
        System.out.println("URL базы данных: " + url);

        Flyway flyway = Flyway.configure()
                .dataSource(url, username, password) // Источник данных (БД)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)  // Создаёт baseline при первом запуске
                .baselineVersion("0")     // Начальная версия миграций
                .validateOnMigrate(false) // Отключает валидацию (для гибкости)
                //.outOfOrder(true)         // Разрешает применять миграции не по порядку
                .load();

        // Запуск миграций
        //flyway.migrate();
        var result = flyway.migrate();
        System.out.println("Миграции Flyway успешно выполнены!");

        // Визуализация результата миграций
        System.out.println("\nРезультат миграции базы данных:");
        System.out.println("+----------------------+----------------------+");
        System.out.printf("| %-20s | %-20s |\n", "Миграций выполнено", "Версия схемы");
        System.out.println("+----------------------+----------------------+");
        System.out.printf("| %-20d | %-20s |\n",
                result.migrationsExecuted,
                result.targetSchemaVersion != null ? result.targetSchemaVersion : "неизвестно");
        System.out.println("+----------------------+----------------------+\n");
    }

    // Подключение к БД, используя данные из конфигурационного файла
    private void connectToDatabase() throws SQLException {
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        // регистрация драйвера
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер PostgreSQL успешно зарегистрирован");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Драйвер PostgreSQL не найден", e);
        }

        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Подключение к БД успешно установлено!");
    }

    private void demonstrateCRUDOperations() throws SQLException {
        System.out.println("\n Демонстрация CRUD операций...");

        // Начинаем транзакцию
        connection.setAutoCommit(false);
        try {
            // 1. Вставка нового товара
            insertNewProduct();

            // 2. Вставка нового покупателя
            int customerId = insertNewCustomer();

            // 3. Создание заказа для покупателя
            createOrder(customerId);

            // 4. Чтение и вывод последних 5 заказов с JOIN на товары и покупателей
            readRecentOrders();

            // 5. Обновление товара
            updateProduct();

            // 6. Удаление тестовых данных
            deleteTestData();

            // Коммитим транзакцию
            connection.commit();
            System.out.println("Все CRUD операции успешно выполнены!");

        } catch (SQLException e) {
            // Откатываем транзакцию при ошибке
            connection.rollback();
            System.err.println("Был произведен откат транзакции из-за ошибки: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Метод для выполнения тестовых запросов к БД
    private void executeTestQueries() {
        System.out.println("\n Выполнение тестовых запросов...");
        System.out.println("=".repeat(80));

        try {
            // 5 ЗАПРОСОВ НА ЧТЕНИЕ

            // Запрос 1: Топ-3 самых популярных товара по количеству проданных единиц
            System.out.println("\n1. Топ-3 самых популярных товара по количеству проданных единиц:");
            System.out.println("-".repeat(80));
            String query1 = """
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
                    LIMIT 3
                """;
            executeAndPrintQuery(query1);

            // Запрос 2: Список всех заказов за последние 7 дней с именем покупателя и описанием товара
            System.out.println("\n2. Список всех заказов за последние 7 дней с именем покупателя и описанием товара:");
            System.out.println("-".repeat(80));
            String query2 = """
                    SELECT
                        o.id AS order_id,
                        TO_CHAR(o.order_date, 'DD.MM.YYYY') order_date,
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
                        o.order_date DESC, o.id
                """;
            executeAndPrintQuery(query2);

            // Запрос 3: Клиенты с количеством заказов и общей суммой покупок
            System.out.println("\n3. Клиенты с количеством заказов и общей суммой покупок:");
            System.out.println("-".repeat(80));
            String query3 = """
                SELECT 
                    c.id,
                    c.first_name || ' ' || c.last_name AS customer_name,
                    c.email,
                    COUNT(o.id) AS total_orders,
                    COALESCE(SUM(o.total_amount), 0) AS total_spent,
                    ROUND(AVG(o.total_amount),2) AS avg_order_value
                FROM customer c
                LEFT JOIN orders o ON c.id = o.customer_id
                GROUP BY c.id, c.first_name, c.last_name, c.email
                ORDER BY total_spent DESC
                """;
            executeAndPrintQuery(query3);

            // Запрос 4: Клиенты, сделавшие более одного заказа
            System.out.println("\n4. Клиенты, сделавшие более одного заказа:");
            System.out.println("-".repeat(80));
            String query4 = """
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
                        orders_count DESC
                """;
            executeAndPrintQuery(query4);

            // Запрос 5: Покупатели с наибольшей суммой покупок,
            //--      сортировка по убыванию суммы покупок покупателя.
            //--      Покупатели без покупок выведены в конце выборки
            System.out.println("\n5. Покупатели с наибольшей суммой покупок:");
            System.out.println("-".repeat(80));
            String query5 = """
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
                """;
            executeAndPrintQuery(query5);

            // 3 ЗАПРОСА НА ИЗМЕНЕНИЕ
            System.out.println("\n Запросы на изменение:");
            System.out.println("=".repeat(80));

            // Запрос 6: Обновление цены товара по ID
            System.out.println("\n6. Обновление цены товара по ID:");
            System.out.println("-".repeat(80));
            String query6 = """
                    UPDATE product
                    SET price = 355.00
                    WHERE id = 16
            """;
            executeUpdateQuery(query6);

            // Запрос 7: Обновление количества на складе при покупке
            System.out.println("\n7. Обновление количества на складе при покупке:");
            System.out.println("-".repeat(80));
            String query7 = """
                    UPDATE product
                    SET quantity = quantity - 1
                    WHERE description LIKE '%Харуки Мураками - Норвежский лес%'
                    """;
            executeUpdateQuery(query7);

            // Запрос 8: Обновление статуса заказа
            System.out.println("\n8. Обновление статуса заказов на 'Доставлен':");
            System.out.println("-".repeat(80));
            String query8 = """
                UPDATE orders 
                SET status_id = (SELECT id FROM order_status WHERE name = 'Доставлен')
                WHERE status_id = (SELECT id FROM order_status WHERE name = 'Отправлен')
                AND order_date < CURRENT_DATE - INTERVAL '2 days'
                """;
            executeUpdateQuery(query8);

            // 2 ЗАПРОСА НА УДАЛЕНИЕ
            System.out.println("\n Запросы на удаление:");
            System.out.println("=".repeat(80));

            // Запрос 9: Удаление клиентов без заказов
            System.out.println("\n9. Удаление клиентов без заказов:");
            System.out.println("-".repeat(80));
            String query9 = "DELETE FROM customer WHERE id NOT IN (SELECT DISTINCT customer_id FROM orders)";
            executeDeleteQuery(query9);

            // Запрос 10: Удаление отмененных заказов старше 10 дней
            System.out.println("\n10. Удаление отмененных заказов старше 10 дней:");
            System.out.println("-".repeat(80));
            String query10 = """
                DELETE FROM orders 
                WHERE status_id = (SELECT id FROM order_status WHERE name = 'Отменен')
                AND order_date < CURRENT_DATE - INTERVAL '10 days'
                """;
            executeDeleteQuery(query10);

        } catch (SQLException e) {
            System.err.println(" Ошибка выполнения тестовых запросов: " + e.getMessage());
        }
    }

    private void executeAndPrintQuery(String query) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Вывод заголовков
            System.out.printf("%-3s ", "#");
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                System.out.printf("%-20s ", shortenString(columnName, 20));
            }
            System.out.println();

            System.out.println("-".repeat(27 * columnCount));

            // Вывод данных
            int rowNum = 1;
            while (rs.next()) {
                System.out.printf("%-3d ", rowNum++);
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    String valueStr = (value != null) ? value.toString() : "NULL";
                    System.out.printf("%-20s ", shortenString(valueStr, 20));
                }
                System.out.println();
            }

            System.out.println("Всего строк: " + (rowNum - 1));
        }
    }

    private void executeUpdateQuery(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(" Обновлено строк: " + rowsAffected);
        }
    }

    private void executeDeleteQuery(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(" Удалено строк: " + rowsAffected);
        }
    }

    private void insertNewProduct() throws SQLException {
        String sql = "INSERT INTO product (description, price, quantity, category) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Мэттью Макконахи - Зеленый свет");
            stmt.setBigDecimal(2, new java.math.BigDecimal("650.00"));
            stmt.setInt(3, 15);
            stmt.setString(4, "Мемуары");

            int rows = stmt.executeUpdate();
            System.out.println("Добавлена новая книга: Мэттью Макконахи - Зеленый свет");
        }
    }

    private int insertNewCustomer() throws SQLException {
        String sql = "INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Людмила");
            stmt.setString(2, "Мельникова");
            stmt.setString(3, "+7 (909) 330-99-13");
            stmt.setString(4, "kollud@mail.ru");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Добавлен новый покупатель с ID: " + id);
                return id;
            }
            throw new SQLException("Не удалось получить ID покупателя");
        }
    }

    private void createOrder(int customerId) throws SQLException {
        String checkProductSQL = "SELECT id FROM product WHERE description LIKE '%Макконахи%' LIMIT 1";
        int productId = 1;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkProductSQL)) {
            if (rs.next()) {
                productId = rs.getInt("id");
            }
        }

        String sql = "INSERT INTO orders (product_id, customer_id, quantity, status_id, total_amount) " +
                "VALUES (?, ?, ?, (SELECT id FROM order_status WHERE name = 'Новый'), ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, customerId);
            stmt.setInt(3, 1);
            stmt.setBigDecimal(4, new java.math.BigDecimal("650.00"));

            int rows = stmt.executeUpdate();
            System.out.println(" Создан новый заказ для покупателя с ID: " + customerId + ", товар ID: " + productId);
        }
    }

    private void readRecentOrders() throws SQLException {
        String sql = """
            SELECT 
                o.id as order_id,
                o.order_date,
                c.first_name || ' ' || c.last_name as customer_name,
                p.description as product_name,
                o.quantity,
                o.total_amount,
                os.name
            FROM orders o
            JOIN customer c ON o.customer_id = c.id
            JOIN product p ON o.product_id = p.id
            JOIN order_status os ON o.status_id = os.id
            ORDER BY o.order_date DESC
            LIMIT 5
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n Чтение и вывод последних 5 заказов с JOIN на товары и покупателей:");
            System.out.println("=".repeat(100));
            System.out.printf("%-8s %-12s %-20s %-25s %-6s %-10s %-12s%n",
                    "ID_Заказа", "Дата", "Покупатель", "Товар", "Кол-во", "Сумма", "Статус");
            System.out.println("=".repeat(100));

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%-8d %-12s %-20s %-25s %-6d %-10.2f %-12s%n",
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date").toLocalDateTime().toLocalDate(),
                        shortenString(rs.getString("customer_name"), 18),
                        shortenString(rs.getString("product_name"), 23),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("name"));
            }

            if (count == 0) {
                System.out.println("Заказы не найдены в базе данных");
            }
            System.out.println("=".repeat(100));
        }
    }

    private String shortenString(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private void updateProduct() throws SQLException {
        String sql = "UPDATE product SET price = price * 1.10, quantity = quantity + 3 WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, 10);
            int rows = stmt.executeUpdate();
            System.out.println("Обновлена цена (+10%) и количество (+3 шт) для книги с ID = 10");
        }
    }

    private void deleteTestData() throws SQLException {
        String deleteOrdersSQL = "DELETE FROM orders WHERE customer_id IN (SELECT id FROM customer WHERE email = 'kollud@mail.ru')";
        String deleteCustomerSQL = "DELETE FROM customer WHERE email = 'kollud@mail.ru'";
        String deleteTestProductSQL = "DELETE FROM product WHERE description = 'Мэттью Макконахи - Зеленый свет'";

        try (Statement stmt = connection.createStatement()) {
            int orderRows = stmt.executeUpdate(deleteOrdersSQL);
            int customerRows = stmt.executeUpdate(deleteCustomerSQL);
            int productRows = stmt.executeUpdate(deleteTestProductSQL);

            System.out.println(" Удалены тестовые данные: " +
                    orderRows + " заказов, " +
                    customerRows + " клиентов, " +
                    productRows + " товаров");
        }
    }

    //Закрытие подключения к БД
    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Подключение к БД закрыто.");
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии подключения: " + e.getMessage());
            }
        }
    }
}
package homeworks.homework06;

import java.util.Objects;

public class Product {
    private String name;
    private double cost;

    // конструктор
    public Product(String name, double cost) {
        setName(name);
        setCost(cost);
    }

    // геттеры и сеттеры
    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void setName(String name) {
        if (name.trim().isEmpty()
        ) {
            System.out.println("Название продукта не может быть пустым!");
            System.exit(0);
        }
        this.name = name.trim();
    }

    public void setCost(double cost) {
        if (cost < 0) {
            System.out.println("Стоимость продукта не может быть отрицательным числом!");
            System.exit(0);
        }
        this.cost = cost;
    }

    // переопределение методов
    @Override
    public String toString() {
        return "Продукт: " + name + "; стоимость = " +
                String.format("%.2f", cost) + " руб.";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return (name.equals(product.getName()) && cost == product.getCost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }
}
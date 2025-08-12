package homeworks.homework06;

import java.util.ArrayList;
import java.util.Objects;

public class Person {
    private String name;
    private double money;
    private final ArrayList<Product> products = new ArrayList<>();

    // конструктор
    public Person(String name, double money) {
        setName(name);
        setMoney(money);
    }

    // геттеры и сеттеры
    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setName(String name) {
        if (name.trim().isEmpty()) {
            System.out.println("Имя не может быть пустым!");
            System.exit(0);
        }
        if (name.trim().length() < 3) {
            System.out.println("Имя не может быть короче 3 символов!");
            System.exit(0);
        }
        this.name = name.trim();
    }

    public void setMoney(double money) {
        if (money < 0) {
            System.out.println("Деньги не могут быть отрицательными!");
            System.exit(0);
        }
        this.money = money;
    }

    public void addProduct(Product product) {
        products.add(product);
        money -= product.getCost();
    }

    // переопределение методов
    @Override
    public String toString() {
        return "Покупатель " + name + " имеет " +
                String.format("%.2f", money) + " руб.";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}

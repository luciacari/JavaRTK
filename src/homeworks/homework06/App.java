package homeworks.homework06;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static final ArrayList<Person> buyersList = new ArrayList<>();
    private static final ArrayList<Product> productsList = new ArrayList<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        //Вводим покупателей и сумму денег, которые они имеют
        addBuyers(scanner);

        if (!buyersList.isEmpty()) {
            //Вводим товары
            //addProducts(scanner);
            //Вводим товары обычные и со скидкой в одной строке
            addDiscountProducts(scanner);

            if (!productsList.isEmpty()) {
                System.out.println("Введите покупки (формат: Имя покупателя - Продукт):");
                //Добавляем продукты в пакет для каждого покупателя
                shopping(scanner);

                // Вывод отчёта о покупках
                purchaseReport();
            }
        }
        scanner.close();
    }

    // метод для ввода покупателей
    private static void addBuyers(Scanner scanner) {
        System.out.println("Введите покупателей (формат: Имя = деньги;) в одну строку");

        String input = scanner.nextLine();

        String[] persons = input.split(";");
        for (String element : persons) {
            String[] array = element.trim().split("=");
            if (array.length > 1) {
                String name = array[0].trim();
                double money = Double.parseDouble(array[1].trim());
                Person pers = new Person(name, money);
                System.out.println(pers);//печать переопределенного метода toString
                boolean flagUnique = true;
                for (Person person : buyersList) {
                    // если покупатель повторился во ввденных данных, то
                    // переприсваиваем покупателю деньги и выходим из цикла, не добавляя в массив
                    if (pers.equals(person)) {
                        flagUnique = false;
                        person.setMoney(money);
                        break;
                    }
                }
                //если покупатель не повторяется во введенных данных,  то
                // присваиваем покупателю деньги и добавляем покупателя в массив
                if (flagUnique) {
                    pers.setMoney(money);
                    buyersList.add(pers);
                }
            } else {
                System.out.println("Ошибка: передана пустая строка вместо данных о покупателях!");
            }
        }
    }

    // метод для ввода продуктов
    private static void addProducts(Scanner scanner) {
        System.out.println("Введите продукты (формат: Наименование = стоимость;) в одну строку:");
        String input = scanner.nextLine();

        String[] products = input.split(";");
        for (String element : products) {
            String[] array = element.trim().split("=");
            if (array.length > 1) {
                String name = array[0].trim();

                double cost = Double.parseDouble(array[1].trim());
                Product p = new Product(name, cost);
                System.out.println(p); //печать переопределенного метода toString
                boolean flagUnique = true;
                for (Product pr : productsList) {
                    // если продукт повторился во ввденных данных, то
                    // переприсваиваем продукту стоимость и выходим из цикла, не добавляя в массив
                    if (p.equals(pr)) {
                        pr.setCost(cost);
                        flagUnique = false;
                        break;
                    }
                }
                //если продукт не повторяется во введенных данных,  то
                // присваиваем продукту стоимость и добавляем продукт в массив
                if (flagUnique) {
                    productsList.add(p);
                }
            } else {
                System.out.println("Ошибка: передана пустая строка вместо данных о продуктах!");
            }
        }
    }

    // метод для ввода продуктов со скидкой (new!)
    private static void addDiscountProducts(Scanner scanner) {
        System.out.println("Введите в одну строку обычные и скидочные продукты " +
                '\n' + "(формат для обычного продукта: Наименование = стоимость ;) "+
                '\n'+  "(формат для скидочного продукта: Наименование = стоимость = % скидки = срок действия (дата yyyy-MM-dd);) :" +
                '\n'+ "Например: Хлеб = 40 = 0.20 = 2025-09-29; Молоко = 60; Торт = 1000 = 0.50 = 2025-09-30; Кофе растворимый = 879; Масло = 150 "
        );
        String input = scanner.nextLine();

        String[] discountProducts = input.split(";");
        for (String element : discountProducts) {
            String[] array = element.trim().split("=");
            if (array.length > 1) {
                String name = array[0].trim();
                double cost = Double.parseDouble(array[1].trim());
                //double discountPercentage = Double.parseDouble(array[2].trim());
                double discountPercentage = array.length > 2 && !array[2].trim().isEmpty()
                        ? Double.parseDouble(array[2].trim())
                        : 0.0; // значение по умолчанию
                //LocalDate discountEndDate = LocalDate.parse(array[3].trim());
                LocalDate discountEndDate = array.length > 3 && !array[3].trim().isEmpty()
                        ? LocalDate.parse(array[3].trim())
                        : LocalDate.now(); // значение по умолчанию
                // если не заполнены 3 и 4 элементы массива продуктов, то это обычный продукт,
                // а не продукт со скидкой
                boolean isDiscountedProduct = array.length > 3 && !array[3].trim().isEmpty();
                Product dp; //объявляем переменную типа Product без инициализации
                if (isDiscountedProduct) {
                    //если продукт со скидкой создаем продукт со скидкой
                    dp = new DiscountProduct(name, cost, discountPercentage, discountEndDate);
                } else {
                    dp = new Product(name, cost);
                }
                System.out.println(dp); //печать переопределенного метода toString для продукта
                boolean flagUnique = true;
                for (Product pr : productsList) {
                    // если продукт повторился во ввденных данных, то
                    // переприсваиваем продукту стоимость и выходим из цикла, не добавляя в массив
                    if (dp.equals(pr)) {
                        pr.setCost(cost);
                        flagUnique = false;
                        break;
                    }
                }
                //если продукт не повторяется во введенных данных,  то
                // присваиваем продукту стоимость и добавляем продукт в массив
                if (flagUnique) {
                    productsList.add(dp);
                }
            } else {
                System.out.println("Ошибка: передана пустая строка вместо данных о продуктах!");
            }
        }
    }

    // метод для совершения покупок
    private static void shopping(Scanner src) {
        int i, buyersListSize, j, productsListSize;
        while (src.hasNext()) {
            String str = src.nextLine();
            if (str.equals("END")) {
                break;
            } else {
                if (str.isEmpty()) continue;

                String[] mass = str.split("-");
                if (mass.length > 1) {
                    String buyer_name = mass[0].trim();
                    String product_name = mass[1].trim();
                    if (!buyer_name.isEmpty() && !product_name.isEmpty()) {
                        buyersListSize = buyersList.size();
                        productsListSize = productsList.size();
                        for (i = 0; i < buyersListSize; i++) {
                            if (buyer_name.equals(buyersList.get(i).getName())) {
                                break;
                            }
                        }
                        for (j = 0; j < productsListSize; j++) {
                            if (product_name.equals(productsList.get(j).getName())) {
                                break;
                            }
                        }

                        if (i < buyersListSize && j < productsListSize) {
                            double price;
                            //Person person = findPersonByName(buyersList.get(i), buyersList.get(i).getName());
                            Product product = findProductByName(productsList.get(j), productsList.get(j).getName());
                            if (buyersList.get(i).getMoney() >= productsList.get(j).getCost()) {
                                buyersList.get(i).addProduct(productsList.get(j));
                                //price = productsList.get(j).getCost();
                                if (product instanceof DiscountProduct) {
                                    // Если это DiscountProduct, получаем цену со скидкой
                                    //System.out.println("product instanceof DiscountProduct");
                                    price = ((DiscountProduct) product).getDiscountedPrice();
                                } else {
                                    // Для обычного продукта используем обычную цену
                                    //System.out.println("обычная цена");
                                    price = productsList.get(j).getCost();
                                }

                                System.out.println(buyersList.get(i).getName() + " купил " + productsList.get(j).getName() + " за " + String.format("%.2f", price) + " руб.");
                                //System.out.println("Остаток денежных средств: " + String.format("%.2f", buyersList.get(i).getMoney()) + " руб.");
                            } else {
                                System.out.println(buyersList.get(i).getName() + " не может позволить себе " + productsList.get(j).getName());
                            }
                        } else {
                            if (i >= buyersListSize) {
                                System.out.println(buyer_name + " отсутствует в списках покупателей");
                            }
                            if (j >= productsListSize) {
                                System.out.println(product_name + " отсутствует в списках продуктов");
                            }
                        }
                    } else {
                        System.out.println("Имя/наименование продукта не может быть пустым");
                    }
                } else {
                    System.out.println("Продукт введен некорректно");
                }
            }
        }
    }

    // метод для вывода отчета о произведенных покупках
    private static void purchaseReport() {
        System.out.println("Отчёт о покупках:");
        for (Person person : buyersList) {
            System.out.print(person.getName() + " - ");

            if (person.getProducts().isEmpty()) {
                System.out.println("Ничего не куплено.");
            } else {
                boolean first = true;
                for (Product product : person.getProducts()) {
                    if (first) first = false;
                    else {
                        System.out.print(", ");
                    }
                    System.out.print(product.getName());
                }
                System.out.println();
            }
        }
    }
    private static Person findPersonByName(Person persons, String name) {
        //for (Person person : persons) {
            if (persons.getName().equals(name)) {
                System.out.println("findPersonByName");
                return persons;
            }
       // }
        return null;
    }

    private static Product findProductByName(Product products, String name) {
        //for (Product product : products) {
            if (products.getName().equals(name)) {
                System.out.println("findProductByName");
                return products;

            }
        //}
        return null;
    }
}
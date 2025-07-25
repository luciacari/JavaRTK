package homeworks.homework04;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Television television1 = new Television("LG 24TQ510S-WZ", 24);
        television1.printInfo();  // Выведет: Модель вашего телевизора: LG 24TQ510S-WZ с диагональю 24'

        Television television2 = new Television("Samsung UE50DU8500UXCE", 50);
        television2.printInfo();

        // Дополнительно. Задавать параметры класса Телевизор с клавиатуры
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите модель телевизора: ");
        String model = scanner.nextLine();
        System.out.println("Введите диагональ телевизора: ");
        int diagonal = scanner.nextInt();

        Television television3 = new Television(model, diagonal);
        television3.printInfo();
    }
}

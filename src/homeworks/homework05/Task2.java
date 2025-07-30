package homeworks.homework05;

import java.util.Scanner;

/*
Задача 2. Задана последовательность, состоящая только из символов ‘>’, ‘<’ и ‘-‘.
Требуется найти количество стрел, которые спрятаны в этой последовательности.
Стрелы – это подстроки вида ‘>>-->’ и ‘<--<<’.
Входные данные: в первой строке входного потока записана строка, состоящая из
символов ‘>’, ‘<’ и ‘-‘ (без пробелов). Строка может содержать до 106 символов.
Выходные данные: в единственную строку выходного потока нужно вывести искомое количество стрелок.
*/
public class Task2 {
    private static final int MAX_LENGTH = 106; //максимально допустимое количество символов
    private static final int LENGTH = 5; // количество символов в "стреле"

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите строку из символов: ‘>’, ‘<’ и ‘-‘ (максимум 106 символов)");
        String input = scanner.nextLine();
        // Проверка, что строка не пустая, иначе ошибка и выход из программы
        if (input.isEmpty()) {
            System.out.println("Ошибка: ничего не введено, завершаем программу!");
            scanner.close(); //Закрываем сканер
            System.exit(0);  // Завершение программы с кодом 0 (без ошибок)
        }
        int inputLength = input.length();
        if (inputLength > MAX_LENGTH) {
            input = input.substring(0, MAX_LENGTH);
            System.out.println("Введенная строка имеет длину " + inputLength + ", поэтому обрезана до 106 символов.");
        }
        inputLength = input.length();
        System.out.println("Введенная строка содержит символы \"" + input + "\" и имеет длину " + input.length());

        int count = 0;
        // В цикле перебираем по 5 символов в подстроке для поиска "стрел"
        for (int i = 0; i <= inputLength - LENGTH; i++) {
            String substr = input.substring(i, i + LENGTH);
            System.out.println("Проверяем позицию " + i + ": " + substr);
            if (substr.equals(">>-->") || substr.equals("<--<<")) {
                count++; // если "стрелка" найдена, увеличиваем счетчик
                System.out.println("Во введенной подстроке найдена стрелка! Текущий счетчик строк: " + count);
            }
        }
        System.out.println("Всего найдено стрелок: " + count);
        scanner.close();
    }
}

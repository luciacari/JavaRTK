package homeworks.homework05;

import java.util.Scanner;

/*
Задача 1. Для введенной с клавиатуры буквы английского алфавита нужно вывести слева
стоящую букву на стандартной клавиатуре. При этом клавиатура замкнута, т.е. справа от буквы «p»
стоит буква «a», а слева от "а" буква "р", также соседними считаются буквы «l» и буква «z»,
а буква «m» с буквой «q».
Входные данные: строка входного потока содержит один символ — маленькую букву английского
алфавита.
Выходные данные: следует вывести букву стоящую слева от заданной буквы, с учетом замкнутости
клавиатуры.
 */
class Keyboard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите маленькую букву английского алфавита: ");
        String input = scanner.nextLine();
        // Проверка, что строка не пустая, иначе ошибка и выход из программы
        if (input.isEmpty()) {
            System.out.println("Ошибка: ничего не введено, завершаем программу!");
            scanner.close(); //Закрываем сканер
            System.exit(0);  // Завершение программы с кодом 0 (без ошибок)
        }
        String keyboardLetters = "qwertyuiopasdfghjklzxcvbnm"; // набор символов замкнутой клавиатуры
        char letter = input.charAt(0); // первый символ из введенной строки
        int index = keyboardLetters.indexOf(letter); //определяем индекс введенного символа из набора
        System.out.println("С клавиатуры введена буква \"" + letter + "\", с индексом = " + index);

        // Проверяем введенный символ на допустимость:
        // должна быть только маленькая буква английского алфавита, иначе - ошибка
        if (index == -1) {
            System.out.println("Ошибка: введен недопустимый символ! Необходимо ввести маленькую букву английского алфавита!");
        } else {
            int leftIndex = (index - 1 + keyboardLetters.length()) % keyboardLetters.length();
            char leftLetter = keyboardLetters.charAt(leftIndex); //получаем символ по индексу
            System.out.println("Буква, слева от введенной с клавиатуры: " + leftLetter);
        }
        scanner.close(); // Закрываем сканер
    }
}

package homeworks.homework07_1109;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/* Задание 2:
С консоли на вход подается две строки s и t. Необходимо вывести true, если одна строка является валидной анаграммой другой строки, и false – если это не так.
Анаграмма – это слово, или фраза, образованная путем перестановки букв другого слова или фразы, обычно с использованием всех исходных букв ровно один раз.
Для проверки:
● Бейсбол - бобслей
● Героин - регион
● Клоака - околка  */
public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите два слова через дефис (например: героин - регион): ");
        String inputString = scanner.nextLine();

        // Разделяем строку по дефису максимум на 2 части
        String[] word = inputString.trim().split("\\s*-\\s*", 2);
        if (word[0].isEmpty() | word[1].isEmpty()) {
            System.out.println("Ошибка: ни одно из слов не может быть пустым!");
            return;
        }
        if (word.length < 2) {
            System.out.println("Ошибка: введите две строки через дефис!");
            return;
        }
        String s = word[0].toLowerCase(); //переводим в нижний регистр
        String t = word[1].toLowerCase();

        // Вызываем метод проверки анаграммы и выводим результат
        System.out.println(isAnagram(s, t));
        scanner.close();
    }

    // Метод проверки анаграммы
    private static boolean isAnagram(String s, String t) {
        // Проверяем на null и на равенство длин
        if (s == null || t == null || s.length() != t.length()) return false;
        // Если строки идентичны, то это не анаграмма
        if (s.equals(t)) return false;

        // Создаем HashMap для подсчета символов
        Map<Character, Integer> myMap = new HashMap();
        // Подсчет символов в первой строке
        for (char ch : s.toCharArray()) {
            // Увеличиваем счетчик символа или устанавливаем 1, если символа нет
            myMap.put(ch, myMap.getOrDefault(ch, 0) + 1);
        }
        // Проверка символов второй строки
        for (char ch : t.toCharArray()) {
            // Если символа нет в первой строке - не анаграмма
            if (!myMap.containsKey(ch)) return false;

            // Уменьшаем счетчик символа
            if (myMap.get(ch) == 1) myMap.remove(ch); //Если счетчик становится 0 - удаляем символ
            else myMap.put(ch, myMap.get(ch) - 1); // Уменьшаем счетчик
        }
        // Если все символы обработаны, myMap должна быть пустой
        return myMap.isEmpty();
    }
}


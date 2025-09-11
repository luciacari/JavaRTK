package homeworks.homework07_1109;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/*Задание 1: Реализовать метод, который на вход принимает ArrayList<T>,
а возвращает набор уникальных элементов этого массива.
Решить, используя коллекции*/
public class Task1 {
    //Дженерик <T> работает с любым типом данных
    public static <T> Set<T> getUniqueElements(ArrayList<T> arrayList) {
        // Используем HashSet, т.к. он автоматически удаляет дубликаты
        Set<T> hashSet = new HashSet<>();

        for (T element : arrayList) {
            hashSet.add(element);
        }
        return hashSet;
    }

    public static void main(String[] args) {
        // Пример для чисел
        ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(28, 1, 2, 30, 28, 5, 1, 6, 5));
        System.out.println("Уникальные числа: " + getUniqueElements(numberList));

        // Пример для строк
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList("Malta", "Russia", "Brazil", "Nepal", "Australia", "Uzbekistan", "Nepal", "Malta", "Brazil"));
        System.out.println("Уникальные строки: " + getUniqueElements(stringList));

        // Пример с объектами
        class Person {
            private String name;

            public Person(String name) {
                this.name = name;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Person person = (Person) o;
                return Objects.equals(name, person.name);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name);
            }

            @Override
            public String toString() {
                return name;
            }
        }
        ArrayList<Person> personList = new ArrayList<>(Arrays.asList(
                new Person("Люда"),
                new Person("Петя"),
                new Person("Люда"),
                new Person("Маша")
        ));
        // Получаем уникальных пользователей
        Set<Person> uniquePersons = getUniqueElements(personList);
        System.out.println("Уникальные пользователи: " + uniquePersons);

        // Пример с пустой коллекцией
        ArrayList<String> emptyList = new ArrayList<>();
        System.out.println("Пустая коллеккция: " + getUniqueElements(emptyList));
    }
}

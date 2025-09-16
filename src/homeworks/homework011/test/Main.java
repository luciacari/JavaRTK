package homeworks.homework011.test;

import homeworks.homework011.model.Car;
import homeworks.homework011.repository.CarsRepository;
import homeworks.homework011.repository.CarsRepositoryImpl;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final String colorToFind = "Black";
    public static final Long mileageToFind = 0L;
    public static final Long n = 700_000L;
    public static final Long m = 800_000L;
    public static final String modelToFind1 = "Toyota";
    public static final String modelToFind2 = "Volvo";

    public static final String INPUT = "/data/cars.txt";
    public static final String OUTPUT = "/data/cars_output.txt";

    public static void main(String[] args) {

        CarsRepository carsRepository = new CarsRepositoryImpl();
        List<Car> loadCarList = carsRepository.loadCarsFromFile(INPUT);
        if (loadCarList.isEmpty()) {
            System.out.println("Файл пустой");
        }

        //Проверка работы в классе Main, методе main.
        System.out.println("Номера автомобилей по цвету или пробегу : " + String.join(" ",
                carsRepository.findCarNumberOrMileage(loadCarList, colorToFind,
                        Integer.valueOf(Math.toIntExact(mileageToFind)))));

        System.out.println("Уникальные автомобили: " +
                carsRepository.carUniqueModel(loadCarList, n, m).size() + " шт.");

        System.out.println("Цвет автомобиля с минимальной стоимостью: " + colorMinCost(loadCarList));

        System.out.println("Средняя стоимость модели " + modelToFind1 + ": "
                + String.format(Locale.GERMAN, "%.2f",
                carsRepository.averagePriceModel(loadCarList, modelToFind1)));
        System.out.println("Средняя стоимость модели " + modelToFind2 + ": "
                + String.format(Locale.GERMAN, "%.2f",
                carsRepository.averagePriceModel(loadCarList, modelToFind2)));
        System.out.println("----------------------------------------------------");

        // Реализация вывода программы в файл *.txt
        carsRepository.saveCarsToFile(OUTPUT, loadCarList);
        System.out.println("Вывод в файл: " + OUTPUT);

        //Создание объекта Java Collections со списком автомобилей.
        List<Car> carList = List.of(
                new Car("a123me", "Mercedes", "White", 0, 8300000),
                new Car("b873of", "Volga", "Black", 0, 673000),
                new Car("w487mn", "Lexus", "Grey", 76000, 900000),
                new Car("p987hj", "Volga", "Red", 610, 704340),
                new Car("c987ss", "Toyota", "White", 254000, 761000),
                new Car("o983op", "Toyota", "Black", 698000, 740000),
                new Car("p146op", "BMW", "White", 271000, 850000),
                new Car("u893ii", "Toyota", "Purple", 210900, 440000),
                new Car("l097df", "Toyota", "Black", 108000, 780000),
                new Car("y876wd", "Toyota", "Black", 160000, 1000000)
        );
        // Используя Java Stream API, вывести:
        // Номера всех автомобилей, имеющих заданный в переменной цвет colorToFind или нулевой пробег mileageToFind.
        System.out.println("Номера автомобилей по цвету или пробегу : " + String.join(" ", findCarNumberOrMileage(carList)));

        // Количество уникальных моделей в ценовом диапазоне от n до m тыс.
        System.out.println("Уникальные автомобили: " + carUniqueModel(carList).size() + " шт.");

        // Вывести цвет автомобиля с минимальной стоимостью.
        System.out.println("Цвет автомобиля с минимальной стоимостью: " + colorMinCost(carList));

        // Среднюю стоимость искомой модели modelToFind
        System.out.println("Средняя стоимость модели " + modelToFind1 + ": "
                + String.format(Locale.GERMAN, "%.2f", averagePriceModel(carList, modelToFind1)));
        System.out.println("Средняя стоимость модели " + modelToFind2 + ": "
                + String.format(Locale.GERMAN, "%.2f", averagePriceModel(carList, modelToFind2)));
    }

    public static Set<String> findCarNumberOrMileage(List<Car> carList) {
        Set<String> mySet = new LinkedHashSet<>();

        List<String> listCarNumberOrMileage = carList.stream()
                .filter(x -> x.getCarColor().startsWith(colorToFind) || x.getCarMileage() == mileageToFind)
                .map(Car::getCarNumber)
                .toList();

        mySet.addAll(listCarNumberOrMileage);
        return mySet;
    }

    public static List<String> carUniqueModel(List<Car> carList) {
        return carList.stream()
                .filter(x -> (x.getCarCost() >= n) && (x.getCarCost() <= m))
                .distinct()
                .map(Car::getCarModel)
                .toList();
    }

    public static String colorMinCost(List<Car> carList) {
        return carList.stream()
                .min(Comparator.comparingLong(Car::getCarCost))
                .map(Car::getCarColor)
                .orElse("Не найдено");
    }

    public static double averagePriceModel(List<Car> carList, String modelToFind) {
        return carList.stream()
                .filter(x -> x.getCarModel().equalsIgnoreCase(modelToFind))
                .collect(Collectors.averagingDouble(Car::getCarCost));
    }
}


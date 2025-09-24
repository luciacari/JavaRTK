package homeworks.homework011.repository;

import homeworks.homework011.model.Car;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

// Выносим методы работы с автомобилем в папку repository:
// интерфейс CarsRepository и его реализацию CarsRepositoryImpl.
public class CarsRepositoryImpl implements CarsRepository {
    private List<Car> cars = new ArrayList<>();

    // Метод получения относительного пути к папке с источником cars.txt
    public String getFullFilePath() {
        // Получаем путь к текущему классу
        String currentClassPath = CarsRepositoryImpl.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();

        // Получаем относительный путь к папке проекта
        Path projectPath = Paths.get(System.getProperty("user.dir"));
        // Получаем относительный путь к папке с Main.java
        Path mainClassPath = Paths.get("src", "homeworks", "homework011");
        // Полный путь к Main.java
        Path fullPath = projectPath.resolve(mainClassPath);

        return fullPath.toString();
    }

    @Override
    public List<Car> loadCarsFromFile(String file) {
        List<Car> loadCars = new ArrayList<>();
        boolean isFirstLine = true;

        file = getFullFilePath() + file.replace("/", "\\");
        //System.out.println("Полный путь к cars.txt: " + file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    loadCars.add(new Car(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim())));
                }
            }
            this.cars = loadCars;
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return loadCars;
    }

    @Override
    public void saveCarsToFile(String file, List<Car> listCars) {

        file = getFullFilePath() + file.replace("/", "\\");
        //System.out.println("Полный путь к cars_output.txt: " + file);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Автомобили в базе:");
            writer.println("Number\tModel\tColor\tMileage\tCost");
            for (Car car : listCars) {
                writer.println(car.toString());
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    @Override
    public Set<String> findCarNumberOrMileage(List<Car> carList, String colorToFind, int mileageToFind) {
        Set<String> mySet = new LinkedHashSet<>();

        List<String> listCarNumberOrMileage = carList.stream()
                .filter(x -> x.getCarColor().startsWith(colorToFind) || x.getCarMileage() == mileageToFind)
                .map(Car::getCarNumber)
                .toList();

        mySet.addAll(listCarNumberOrMileage);
        return mySet;
    }

    @Override
    public List<String> carUniqueModel(List<Car> carList, long n, long m) {
        return carList.stream()
                .filter(x -> (x.getCarCost() >= n) && (x.getCarCost() <= m))
                .distinct()
                .map(Car::getCarModel)
                .toList();
    }

    @Override
    public String colorMinCost(List<Car> carList) {
        return carList.stream()
                .min(Comparator.comparingLong(Car::getCarCost))
                .map(Car::getCarColor)
                .orElse("Не найдено");
    }

    @Override
    public double averagePriceModel(List<Car> carList, String modelToFind) {
        return carList.stream()
                .filter(x -> x.getCarModel().equalsIgnoreCase(modelToFind))
                .collect(Collectors.averagingDouble(Car::getCarCost));
    }
}

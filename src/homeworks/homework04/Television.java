package homeworks.homework04;

public class Television {
    private String model;
    private int diagonal;

    // Конструктор
    public Television(String model, int diagonal) {
        this.model = model;
        this.diagonal = diagonal;
    }

    // Геттеры и сеттеры

    public String getModel() {
        return model;
    }

    public int getDiagonal() {
        return diagonal;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setDiagonal(int diagonal) {
        this.diagonal = diagonal;
    }

    // Метод для печати информации
    public void printInfo() {
        System.out.println("Модель вашего телевизора: " + model + " с диагональю " + diagonal + "\".");
    }

}

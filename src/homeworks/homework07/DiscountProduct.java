package homeworks.homework07;

import java.time.LocalDate;
import java.util.Objects;

public class DiscountProduct extends Product {
    private double sizeDiscount; // Размер скидки (например, 0.20 = 20%)
    private LocalDate expiryDate; // Срок действия скидки yyyy-MM-dd

    public DiscountProduct(String name, double cost, double sizeDiscount, LocalDate expiryDate) {
        super(name, cost);

        if (sizeDiscount < 0 || sizeDiscount >= 1) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до 1 (например, 0.15 для 15%)");
        }

        if (expiryDate == null) {
            throw new IllegalArgumentException("Дата окончания скидки должна быть указана");
        }

        this.sizeDiscount = sizeDiscount;
        this.expiryDate = expiryDate;
    }

    public double getSizeDiscount() {
        return sizeDiscount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public double getDiscountedPrice() {
        // Если скидка ещё действует, применяем её
        if (LocalDate.now().isBefore(expiryDate)) {
            return (double) Math.round(getCost() * (1 - sizeDiscount));
        }
        // Если скидка закончилась, возвращаем полную цену
        return getCost();
    }

    @Override
    public String toString() {
        return "Продукт со скидкой: " + getName() +
                "; стоимость = " + String.format("%.2f", getCost()) + " руб." +
                "; % скидки = " + sizeDiscount + "; " +
                "срок действия скидки до = " + expiryDate +
                "; цена со скидкой = " + String.format("%.2f", getDiscountedPrice()) + " руб" +
                '.';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountProduct)) return false;
        if (!super.equals(o)) return false;
        DiscountProduct that = (DiscountProduct) o;
        return Double.compare(that.sizeDiscount, sizeDiscount) == 0 &&
                expiryDate.equals(that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sizeDiscount, expiryDate);
    }

}

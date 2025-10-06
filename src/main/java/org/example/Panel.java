package org.example;

public class Panel extends RealEstate implements PanelInterface {

    private int floor;
    private boolean isInsulated;

    public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre,
                 int floor, boolean isInsulated) {
        super(city, price, sqm, numberOfRooms, genre);
        this.floor = floor;
        this.isInsulated = isInsulated;
    }

    // Override getTotalPrice with floor & insulation modifiers
    @Override
    public int getTotalPrice() {
        double total = super.getTotalPrice();

        if (floor >= 0 && floor <= 2) {
            total *= 1.05;
        } else if (floor == 10) {
            total *= 0.95;
        }

        if (isInsulated) {
            total *= 1.05;
        }

        return (int) Math.round(total);
    }

    // Check if another org.example.RealEstate has the same total price
    @Override
    public boolean hasSameAmount(RealEstate other) {
        return this.getTotalPrice() == other.getTotalPrice();
    }

    // Average room price (ignores modifiers)
    @Override
    public int roomprice() {
        if (numberOfRooms <= 0) return 0;
        double basePrice = price * sqm;
        return (int) Math.round(basePrice / numberOfRooms);
    }

    @Override
    public String toString() {
        return "Panel{" +
                "city='" + city + '\'' +
                ", pricePerSqm=" + price +
                ", sqm=" + sqm +
                ", numberOfRooms=" + numberOfRooms +
                ", genre=" + genre +
                ", floor=" + floor +
                ", insulated=" + isInsulated +
                ", totalPrice=" + getTotalPrice() +
                ", avgSqmPerRoom=" + String.format("%.2f", averageSqmPerRoom()) +
                ", roomPrice=" + roomprice() +
                '}';
    }
}

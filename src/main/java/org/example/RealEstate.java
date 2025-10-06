package org.example;

public class RealEstate implements PropertyInterface {

    protected String city;
    protected double price; // price per sqm
    protected int sqm;
    protected double numberOfRooms;
    protected Genre genre;

    // Enum for property type
    public static enum Genre {
        FAMILYHOUSE, CONDOMINIUM, FARM
    }

    // Constructor
    public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
        this.city = city;
        this.price = price;
        this.sqm = sqm;
        this.numberOfRooms = numberOfRooms;
        this.genre = genre;
    }

    // Apply discount on price per sqm
    @Override
    public void makeDiscount(int percentage) {
        if (percentage > 0 && percentage <= 100) {
            price -= price * percentage / 100.0;
        }
    }

    // Calculate total price with city modifiers
    @Override
    public int getTotalPrice() {
        double basePrice = price * sqm;
        switch (city.toLowerCase()) {
            case "budapest":
                basePrice *= 1.30;
                break;
            case "debrecen":
                basePrice *= 1.20;
                break;
            case "nyíregyháza":
                basePrice *= 1.15;
                break;
            default:
                break;
        }
        return (int) Math.round(basePrice);
    }

    // Average sqm per room
    @Override
    public double averageSqmPerRoom() {
        if (numberOfRooms <= 0) return 0;
        return (double) sqm / numberOfRooms;
    }

    @Override
    public String toString() {
        return "org.example.RealEstate{" +
                "city='" + city + '\'' +
                ", pricePerSqm=" + price +
                ", sqm=" + sqm +
                ", numberOfRooms=" + numberOfRooms +
                ", genre=" + genre +
                ", totalPrice=" + getTotalPrice() +
                ", avgSqmPerRoom=" + String.format("%.2f", averageSqmPerRoom()) +
                '}';
    }
}
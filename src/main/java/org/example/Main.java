package org.example;

public class Main {
    public static void main(String[] args) {
        RealEstate house = new RealEstate("Budapest", 1000, 120, 4, RealEstate.Genre.FAMILYHOUSE);
        Panel panel = new Panel("Debrecen", 800, 80, 3, RealEstate.Genre.CONDOMINIUM, 10, true);

        System.out.println(house);
        System.out.println(panel);

        house.makeDiscount(10);
        System.out.println("After discount: " + house);

        System.out.println("Same price? " + panel.hasSameAmount(house));
    }
}
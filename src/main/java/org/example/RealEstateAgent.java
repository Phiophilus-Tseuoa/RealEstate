package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RealEstateAgent {

    private static TreeSet<RealEstate> properties = new TreeSet<>(Comparator.comparingInt(RealEstate::getTotalPrice));

    public static void loadProperties(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                RealEstate property = parseProperty(line);
                if (property != null) {
                    properties.add(property);
                }
            }
        } catch (IOException e) {
            System.out.println("File not found. Loading fallback data...");
            loadFallbackData();
        }
    }

    private static RealEstate parseProperty(String line) {
        String[] parts = line.split("#");
        try {
            String type = parts[0].toUpperCase();
            String city = parts[1];
            double price = Double.parseDouble(parts[2]);
            int sqm = Integer.parseInt(parts[3]);
            double rooms = Double.parseDouble(parts[4]);
            RealEstate.Genre genre = RealEstate.Genre.valueOf(parts[5].toUpperCase());

            if (type.equals("PANEL")) {
                int floor = Integer.parseInt(parts[6]);
                boolean isInsulated = parts[7].equalsIgnoreCase("yes");
                return new Panel(city, price, sqm, rooms, genre, floor, isInsulated);
            } else {
                return new RealEstate(city, price, sqm, rooms, genre);
            }
        } catch (Exception e) {
            System.out.println("Error parsing line: " + line);
            return null;
        }
    }

    private static void loadFallbackData() {
        String[] fallback = {
                "REALESTATE#Budapest#250000#100#4#FLAT",
                "REALESTATE#Debrecen#220000#120#5#FAMILYHOUSE",
                "REALESTATE#Nyíregyháza#110000#60#2#FARM",
                "REALESTATE#Nyíregyháza#250000#160#6#FAMILYHOUSE",
                "REALESTATE#Kisvárda#150000#50#2#FLAT",
                "REALESTATE#Nyíregyháza#150000#68#4#FLAT",
                "PANEL#Budapest#180000#70#3#FLAT#4#no",
                "PANEL#Debrecen#120000#35#2#FLAT#0#yes",
                "PANEL#Tiszaújváros#120000#750#3#FLAT#10#no",
                "PANEL#Nyíregyháza#170000#80#3#FLAT#7#no"
        };

        for (String line : fallback) {
            RealEstate property = parseProperty(line);
            if (property != null) {
                properties.add(property);
            }
        }
    }

    public static void displayProperties() {
        for (RealEstate property : properties) {
            System.out.println(property);
        }
    }

    public static void generateReport(String outputFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            // 1. Average square meter price
            double avgPricePerSqm = properties.stream()
                    .mapToDouble(p -> p.price)
                    .average()
                    .orElse(0);
            writer.println("Average price per square meter: " + String.format("%.2f", avgPricePerSqm));
            System.out.println("Average price per square meter: " + String.format("%.2f", avgPricePerSqm));

            // 2. Cheapest property
            RealEstate cheapest = properties.stream()
                    .min(Comparator.comparingInt(RealEstate::getTotalPrice))
                    .orElse(null);
            if (cheapest != null) {
                writer.println("Cheapest property price: " + cheapest.getTotalPrice());
                System.out.println("Cheapest property price: " + cheapest.getTotalPrice());
            }

            // 3. Avg sqm per room of most expensive Budapest apartment
            RealEstate budapestMax = properties.stream()
                    .filter(p -> p.city.equalsIgnoreCase("Budapest"))
                    .max(Comparator.comparingInt(RealEstate::getTotalPrice))
                    .orElse(null);
            if (budapestMax != null) {
                writer.println("Avg sqm per room of most expensive Budapest property: " +
                        String.format("%.2f", budapestMax.averageSqmPerRoom()));
                System.out.println("Avg sqm per room of most expensive Budapest property: " +
                        String.format("%.2f", budapestMax.averageSqmPerRoom()));
            }

            // 4. Total price of all properties
            int totalPrice = properties.stream()
                    .mapToInt(RealEstate::getTotalPrice)
                    .sum();
            writer.println("Total price of all properties: " + totalPrice);
            System.out.println("Total price of all properties: " + totalPrice);

            // 5. Condominiums below average price
            double avgTotalPrice = properties.stream()
                    .mapToInt(RealEstate::getTotalPrice)
                    .average()
                    .orElse(0);

            List<RealEstate> affordableCondos = properties.stream()
                    .filter(p -> p.genre == RealEstate.Genre.CONDOMINIUM)
                    .filter(p -> p.getTotalPrice() <= avgTotalPrice)
                    .toList();

            writer.println("Condominiums below average total price:");
            System.out.println("Condominiums below average total price:");
            for (RealEstate condo : affordableCondos) {
                writer.println(condo);
                System.out.println(condo);
            }

            // 6. Repeat average sqm price
            writer.println("Repeated average price per square meter: " + String.format("%.2f", avgPricePerSqm));
            System.out.println("Repeated average price per square meter: " + String.format("%.2f", avgPricePerSqm));

            // 7. Repeat total price
            writer.println("Repeated total price of all properties: " + totalPrice);
            System.out.println("Repeated total price of all properties: " + totalPrice);

        } catch (IOException e) {
            System.out.println("Error writing to file: " + outputFile);
        }
    }
    // Return all properties (for testing or external use)
    public static TreeSet<RealEstate> getProperties() {
        return properties;
    }

    // 1. Average price per square meter
    public static double getAveragePricePerSqm() {
        return properties.stream()
                .mapToDouble(p -> p.price)
                .average()
                .orElse(0);
    }

    // 2. Cheapest property
    public static RealEstate getCheapestProperty() {
        return properties.stream()
                .min(Comparator.comparingInt(RealEstate::getTotalPrice))
                .orElse(null);
    }

    // 3. Avg sqm per room of most expensive Budapest property
    public static double getAvgSqmPerRoomOfMostExpensiveBudapest() {
        return properties.stream()
                .filter(p -> p.city.equalsIgnoreCase("Budapest"))
                .max(Comparator.comparingInt(RealEstate::getTotalPrice))
                .map(RealEstate::averageSqmPerRoom)
                .orElse((double) 0);
    }

    // 4. Total price of all properties
    public static int getTotalPriceOfAllProperties() {
        return properties.stream()
                .mapToInt(RealEstate::getTotalPrice)
                .sum();
    }

    // 5. Average total price
    public static double getAverageTotalPrice() {
        return properties.stream()
                .mapToInt(RealEstate::getTotalPrice)
                .average()
                .orElse(0);
    }

    // 6. Condominiums below average total price
    public static List<RealEstate> getAffordableCondominiums() {
        double avgTotal = getAverageTotalPrice();
        return properties.stream()
                .filter(p -> p.genre == RealEstate.Genre.CONDOMINIUM)
                .filter(p -> p.getTotalPrice() <= avgTotal)
                .toList();
    }
}

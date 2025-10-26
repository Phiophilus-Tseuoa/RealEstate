package org.example;

public class Main {
    public static void main(String[] args) {
        RealEstateAgent.loadProperties("realestates.txt");
        RealEstateAgent.generateReport("outputRealEstate.txt");
    }
}
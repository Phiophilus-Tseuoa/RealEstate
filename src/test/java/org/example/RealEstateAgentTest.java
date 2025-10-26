package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RealEstateAgentTest {

    @BeforeAll
    public static void setup() {
        RealEstateAgent.loadProperties("realestates.txt"); // Use fallback if file missing
    }

    @Test
    public void testAveragePricePerSqm() {
        double avg = RealEstateAgent.getAveragePricePerSqm();
        assertTrue(avg > 0, "Average price per sqm should be positive");
    }

    @Test
    public void testCheapestProperty() {
        RealEstate cheapest = RealEstateAgent.getCheapestProperty();
        assertNotNull(cheapest, "Cheapest property should not be null");
        int minPrice = cheapest.getTotalPrice();
        for (RealEstate p : RealEstateAgent.getProperties()) {
            assertTrue(minPrice <= p.getTotalPrice(), "Cheapest property should have lowest price");
        }
    }

    @Test
    public void testMostExpensiveBudapestPropertyAvgSqmPerRoom() {
        double avg = RealEstateAgent.getAvgSqmPerRoomOfMostExpensiveBudapest();
        assertTrue(avg >= 0, "Average sqm per room should be non-negative");
    }

    @Test
    public void testTotalPriceOfAllProperties() {
        int total = RealEstateAgent.getTotalPriceOfAllProperties();
        assertTrue(total > 0, "Total price should be positive");
    }

    @Test
    public void testAffordableCondominiums() {
        List<RealEstate> condos = RealEstateAgent.getAffordableCondominiums();
        double avgTotal = RealEstateAgent.getAverageTotalPrice();
        for (RealEstate condo : condos) {
            assertEquals(RealEstate.Genre.CONDOMINIUM, condo.genre, "Should be a condominium");
            assertTrue(condo.getTotalPrice() <= avgTotal, "Should be below average total price");
        }
    }
}

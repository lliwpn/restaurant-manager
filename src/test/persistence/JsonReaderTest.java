package persistence;

/*
Description: JSONReaderTest basis
Author: CPSC210 Team / Paul Carter
Date: October 17th, 2020
URL:https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java
 */

import model.Menu;
import model.RestaurantSession;
import model.exceptions.ConstructionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void readNonExistentTest() {
        JsonReader reader = new JsonReader("./data/noFile.json");
        try {
            RestaurantSession rs = reader.readRestaurant();
            fail("exception expected");
        } catch (IOException e) {
            // success
        } catch (ConstructionException e) {
            fail("file should not be parsed");
        }

        try {
            Menu m = reader.readMenu();
            fail("exception expected");
        } catch (IOException e) {
            // success
        } catch (ConstructionException e) {
            fail("file should not be parsed");
        }
    }

    @Test
    void readEmptyTest() {
        JsonReader reader = new JsonReader("./data/testReadEmpty.json");
        try {
            RestaurantSession rs = reader.readRestaurant();
            assertEquals(0, rs.getCurrentCustomers().size());
            assertEquals(0, rs.getTallyTotalEarnings());
            assertEquals(0, rs.getTallyOperatingCosts());
            assertEquals(0, rs.getTallyNetProfit());
        } catch (IOException e) {
            fail("file read error");
        } catch (ConstructionException e) {
            fail("construction error");
        }

        try {
            Menu m = reader.readMenu();
            assertEquals(0, m.getItems().size());
        } catch (IOException e) {
            fail("file read error");
        } catch (ConstructionException e) {
            fail("construction error");
        }
    }

    @Test
    void readNonEmptyTest() {
        JsonReader reader = new JsonReader("./data/testReadNonEmpty.json");
        try {
            RestaurantSession rs = reader.readRestaurant();
            assertEquals(1, rs.getCurrentCustomers().size());
            assertEquals("customer", rs.getCurrentCustomers().get(0).getName());
            assertEquals(30, rs.getCurrentCustomers().get(0).getAge());
            assertEquals(1230.57, rs.getTallyTotalEarnings());
            assertEquals(503.80, rs.getTallyOperatingCosts());
            assertEquals(726.77, rs.getTallyNetProfit());
        } catch (IOException e) {
            fail("file read error");
        } catch (ConstructionException e) {
            fail("construction error");
        }

        try {
            Menu m = reader.readMenu();
            assertEquals(2, m.getItems().size());
            assertEquals("platefetch", m.getItems().get(0).getId());
            assertEquals(1.5, m.getItems().get(0).getPrice());
            assertEquals(1, m.getItems().get(0).getOperatingPrice());
            assertEquals("platefetch2", m.getItems().get(1).getId());
            assertEquals(6, m.getItems().get(1).getPrice());
            assertEquals(5, m.getItems().get(1).getOperatingPrice());
        } catch (IOException e) {
            fail("file read error");
        } catch (ConstructionException e) {
            fail("construction error");
        }
    }
}

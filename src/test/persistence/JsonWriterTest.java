package persistence;

/*
Description: JSONWriterTest basis
Author: CPSC210 Team / Paul Carter
Date: October 17th, 2020
URL:https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
 */

import model.*;
import model.exceptions.ConstructionException;
import model.exceptions.CustomerNotInListException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    Plate redPlate;
    Plate bluePlate;
    Customer customerA;
    Customer customerB;

    @BeforeEach
    void setup() {
        try {
            redPlate = new Plate("red", 2, 1);
            bluePlate = new Plate("blue", 10, 6);
            customerA = new Customer(1, "customerA");
            customerB = new Customer(2, "customerB");

            customerA.addPlate(redPlate);
            customerA.addPlate(redPlate);
            customerA.addPlate(bluePlate);

            customerB.addPlate(bluePlate);
        } catch (Exception e) {
            fail("construction error");
        }
    }

    @Test
    void writerInvalidTest() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // success
        }
    }

    @Test
    void writerEmptyTest() {
        try {
            RestaurantSession rs = new RestaurantSession();
            Menu m = new Menu();
            JsonWriter writer = new JsonWriter("./data/testEmpty");
            writer.open();
            writer.write(rs, m);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmpty");
            rs = reader.readRestaurant();
            m = reader.readMenu();
            assertEquals(0, rs.getCurrentCustomers().size());
            assertEquals(0, m.getItems().size());
        } catch (IOException e) {
            fail("exception was thrown");
        } catch (ConstructionException e) {
            fail("reconstruction error");
        }
    }

    @Test
    void writerNonEmptyTest() {
        try {
            RestaurantSession rs = new RestaurantSession();
            Menu m = new Menu();
            JsonWriter writer = new JsonWriter("./data/testNonEmpty");

            m.addItem(redPlate);
            m.addItem(bluePlate);
            rs.customerEnters(customerA);
            rs.customerEnters(customerB);
            rs.customerLeaves(customerA);

            writer.open();
            writer.write(rs, m);
            writer.close();

            JsonReader reader = new JsonReader("./data/testNonEmpty");
            rs = reader.readRestaurant();
            m = reader.readMenu();

            Customer customerBRead = rs.getCurrentCustomers().get(0);

            assertEquals("customerB", customerBRead.getName());
            assertEquals(2, customerBRead.getAge());
            assertEquals(1, rs.getCurrentCustomers().size());
            assertEquals(14, rs.getTallyTotalEarnings());
            assertEquals(8, rs.getTallyOperatingCosts());
            assertEquals(6, rs.getTallyNetProfit());

            Plate redPlateRead = m.getItems().get(0);
            Plate bluePlateRead = m.getItems().get(1);
            assertEquals(2, m.getItems().size());

            assertEquals("red", redPlateRead.getId());
            assertEquals(2, redPlateRead.getPrice());
            assertEquals(1, redPlateRead.getOperatingPrice());

            assertEquals("blue", bluePlateRead.getId());
            assertEquals(10, bluePlateRead.getPrice());
            assertEquals(6, bluePlateRead.getOperatingPrice());
        } catch (IOException e) {
            fail("IOException was thrown");
        } catch (ConstructionException e) {
            fail("internal reconstruction error");
        } catch (CustomerNotInListException e) {
            fail("setup construction error");
        }
    }
}

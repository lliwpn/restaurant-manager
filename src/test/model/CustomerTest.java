package model;

import model.exceptions.AgeInvalidException;
import model.exceptions.MoneyFormatException;
import model.exceptions.ProfitErrException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer1;
    Customer customer2;

    Plate redPlate;
    Plate greenPlate;
    Plate bluePlate;

    @BeforeEach
    public void setup() {
        try {
            customer1 = new Customer(35, "John Smith");
            customer2 = new Customer(7, "Ellis Williams");

            redPlate = new Plate("red", 5.00, 1.50);
            greenPlate = new Plate("green", 1.50, 0.50);
            bluePlate = new Plate("blue", 0, 0);
        } catch (AgeInvalidException e) {
            fail("age invalid");
        } catch (MoneyFormatException e) {
            fail("money format");
        } catch (ProfitErrException e) {
            fail("profit (operating > price) error");
        }
    }

    @Test
    public void customerInvalidTest() {
        try {
            customer1 = new Customer(-4, "Jane");
            fail("exception not thrown");
        } catch (AgeInvalidException e) {
            // success
        }
    }

    @Test
    public void customerTest() {
        assertEquals(35, customer1.getAge());
        assertEquals("John Smith", customer1.getName());

        assertEquals(7, customer2.getAge());
        assertEquals("Ellis Williams", customer2.getName());

        customer1.setAge(36);
        customer2.setName("Elisa Williams");

        assertEquals(36, customer1.getAge());
        assertEquals("Elisa Williams", customer2.getName());
    }

    @Test
    public void unmodifiedGetPlatesTest() {
        ArrayList<Plate> plates = customer1.getPlates();

        plates.add(redPlate);
        assertFalse(customer1.getPlates().contains(redPlate));
        assertEquals(0, customer1.getPlates().size());
    }

    @Test
    public void billTallyEmptyTest() {
        double billTally = customer1.billTally();
        assertEquals(0, billTally);
    }

    @Test
    public void billTallyNonEmptyTest() {
        customer1.addPlate(redPlate);

        assertEquals(5.00, customer1.billTally());

        customer1.addPlate(redPlate);
        customer1.addPlate(bluePlate);
        customer1.addPlate(greenPlate);
        customer1.addPlate(greenPlate);

        assertEquals(13.00, customer1.billTally());
    }

    @Test
    public void operatingCostForCustomerTallyEmptyTest() {
        assertEquals(0, customer1.operatingCostForCustomerTally());
        customer1.addPlate(bluePlate);
        assertEquals(0, customer1.operatingCostForCustomerTally());
    }

    @Test
    public void operatingCostForCustomerTallyNonEmptyTest() {
        customer1.addPlate(redPlate);

        assertEquals(1.50, customer1.operatingCostForCustomerTally());

        customer1.addPlate(redPlate);
        customer1.addPlate(bluePlate);
        customer1.addPlate(greenPlate);
        customer1.addPlate(greenPlate);

        assertEquals(4, customer1.operatingCostForCustomerTally());
    }

    @Test
    public void zeroNetProfitTallyTest() {
        assertEquals(0, customer1.netProfitTally());
        customer1.addPlate(bluePlate);
        assertEquals(0, customer1.netProfitTally());
    }

    @Test
    public void hasNetProfitTallyTest() {
        customer1.addPlate(redPlate);
        customer1.addPlate(greenPlate);
        customer1.addPlate(bluePlate);
        assertEquals(4.5, customer1.netProfitTally());

        customer1.addPlate(redPlate);
        assertEquals(8, customer1.netProfitTally());
    }

    @Test
    public void addPlateSingleTest() {
        customer1.addPlate(redPlate);

        ArrayList<Plate> customerPlates = customer1.getPlates();
        assertEquals(1, customerPlates.size());
        assertEquals(redPlate, customerPlates.get(0));
    }

    @Test
    public void addPlateMultipleTest() {
        customer1.addPlate(redPlate);
        customer1.addPlate(bluePlate);
        customer1.addPlate(redPlate);
        customer1.addPlate(greenPlate);

        ArrayList<Plate> customerPlates = customer1.getPlates();
        assertEquals(4, customerPlates.size());
        assertEquals(redPlate, customerPlates.get(0));
        assertEquals(bluePlate, customerPlates.get(1));
        assertEquals(redPlate, customerPlates.get(2));
        assertEquals(greenPlate, customerPlates.get(3));
    }

    @Test
    public void resetPlatesNoneDefaultTest() {
        customer1.resetPlates();

        ArrayList<Plate> customerPlates = customer1.getPlates();
        assertEquals(0, customerPlates.size());
    }

    @Test
    public void resetPlatesTest() {
        customer2.addPlate(bluePlate);
        customer2.addPlate(greenPlate);

        ArrayList<Plate> customerPlates = customer2.getPlates();
        assertEquals(2, customerPlates.size());

        customer2.resetPlates();

        customerPlates = customer2.getPlates();
        assertEquals(0, customerPlates.size());
    }

    @Test
    public void toJsonTest() {
        JSONObject customer1Json = customer1.toJson();

        assertEquals("John Smith", customer1Json.getString("name"));
        assertEquals(35, customer1Json.getInt("age"));
        assertEquals(0, customer1Json.getJSONArray("plates").length());

        customer2.addPlate(greenPlate);
        customer2.addPlate(greenPlate);
        JSONObject customer2Json = customer2.toJson();

        assertEquals("Ellis Williams", customer2Json.getString("name"));
        assertEquals(7, customer2Json.getInt("age"));
        assertEquals(2, customer2Json.getJSONArray("plates").length());
    }
}
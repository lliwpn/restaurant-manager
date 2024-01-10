package model;

import model.exceptions.AgeInvalidException;
import model.exceptions.CustomerNotInListException;
import model.exceptions.MoneyFormatException;
import model.exceptions.ProfitErrException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantSessionTest {

    RestaurantSession restaurantSession;

    Customer customer1;
    Customer customer2;
    Plate redPlate;
    Plate bluePlate;
    Plate greenPlate;

    @BeforeEach
    public void setup() {
        restaurantSession = new RestaurantSession();

        try {
            customer1 = new Customer(18, "Person A");
            customer2 = new Customer(40, "Person B");

            redPlate = new Plate("red", 3.00, 2.00);
            bluePlate = new Plate("blue", 7.00, 3.00);
            greenPlate = new Plate("green", 5.00, 2.50);
        } catch (AgeInvalidException e) {
            fail("age invalid");
        } catch (MoneyFormatException e) {
            fail("money error");
        } catch (ProfitErrException e) {
            fail("profit error");
        }

        customer1.addPlate(redPlate);
        customer1.addPlate(bluePlate);
        customer1.addPlate(greenPlate);
    }

    @Test
    public void restaurantSessionTest() {
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());
        assertEquals(0, restaurantSession.getCurrentCustomers().size());
    }

    @Test
    public void rsDuplicateTest() {
        ArrayList<Customer> customerList = new ArrayList<>();
        customerList.add(customer1);
        customerList.add(customer2);
        RestaurantSession restaurantSessionDuplicate = new RestaurantSession(customerList, 10.50, 5, 5.5);

        assertEquals(2, restaurantSessionDuplicate.getCurrentCustomers().size());
        assertEquals(10.50, restaurantSessionDuplicate.getTallyTotalEarnings());
        assertEquals(5, restaurantSessionDuplicate.getTallyOperatingCosts());
        assertEquals(5.5, restaurantSessionDuplicate.getTallyNetProfit());
    }

    @Test
    public void setterTest() {
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());

        restaurantSession.setTallyNetProfit(1);
        restaurantSession.setTallyOperatingCosts(2);
        restaurantSession.setTallyTotalEarnings(3);

        assertEquals(3, restaurantSession.getTallyTotalEarnings());
        assertEquals(2, restaurantSession.getTallyOperatingCosts());
        assertEquals(1, restaurantSession.getTallyNetProfit());
    }

    @Test
    public void singleCustomerEntersTest() {
        assertEquals(0, restaurantSession.getCurrentCustomers().size());
        restaurantSession.customerEnters(customer2);
        assertEquals(1, restaurantSession.getCurrentCustomers().size());
        assertEquals(customer2, restaurantSession.getCurrentCustomers().get(0));
    }

    @Test
    public void multipleCustomersEnterTest() {
        restaurantSession.customerEnters(customer1);
        restaurantSession.customerEnters(customer2);

        ArrayList<Customer> customers = restaurantSession.getCurrentCustomers();
        assertEquals(2, customers.size());
        assertEquals(customer1, customers.get(0));
        assertEquals(customer2, customers.get(1));
    }

    @Test
    public void customerLeavesDidNothingTest() {
        assertEquals(0, restaurantSession.getCurrentCustomers().size());

        restaurantSession.customerEnters(customer2);
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());
        assertEquals(1, restaurantSession.getCurrentCustomers().size());

        try {
            restaurantSession.customerLeaves(customer2);
        } catch (CustomerNotInListException e) {
            fail("customer should be in list");
        }
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());
        assertEquals(0, restaurantSession.getCurrentCustomers().size());
    }

    @Test
    public void singleCustomerLeavesDidSomethingTest() {
        assertEquals(0, restaurantSession.getCurrentCustomers().size());

        restaurantSession.customerEnters(customer1);
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());
        assertEquals(1, restaurantSession.getCurrentCustomers().size());

        try {
            restaurantSession.customerLeaves(customer1);
        } catch (CustomerNotInListException e) {
            fail("customer should be in the list");
        }
        assertEquals(15, restaurantSession.getTallyTotalEarnings());
        assertEquals(7.5, restaurantSession.getTallyOperatingCosts());
        assertEquals(7.5, restaurantSession.getTallyNetProfit());
        assertEquals(0, restaurantSession.getCurrentCustomers().size());
    }

    @Test
    public void multipleCustomersLeaveDidSomethingTest() {
        assertEquals(0, restaurantSession.getCurrentCustomers().size());

        customer2.addPlate(redPlate);
        customer2.addPlate(redPlate);
        customer2.addPlate(redPlate);
        restaurantSession.customerEnters(customer1);
        restaurantSession.customerEnters(customer2);
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());
        assertEquals(2, restaurantSession.getCurrentCustomers().size());

        try {
            restaurantSession.customerLeaves(customer1);
            restaurantSession.customerLeaves(customer2);
        } catch (CustomerNotInListException e) {
            fail("customers should be in list");
        }

        assertEquals(24, restaurantSession.getTallyTotalEarnings());
        assertEquals(13.5, restaurantSession.getTallyOperatingCosts());
        assertEquals(10.5, restaurantSession.getTallyNetProfit());
    }

    @Test
    public void customerLeavesNotInList() {
        try {
            restaurantSession.customerLeaves(customer1);
            fail("exception was not thrown");
        } catch (CustomerNotInListException e) {
            // success
        }
    }

    @Test
    public void resetTest() {
        restaurantSession.customerEnters(customer1);
        restaurantSession.customerEnters(customer2);
        try {
            restaurantSession.customerLeaves(customer1);
        } catch (CustomerNotInListException e) {
            fail("exception should not be thrown");
        }
        assertEquals(1, restaurantSession.getCurrentCustomers().size());
        assertEquals(15, restaurantSession.getTallyTotalEarnings());
        assertEquals(7.5, restaurantSession.getTallyOperatingCosts());
        assertEquals(7.5, restaurantSession.getTallyNetProfit());

        restaurantSession.reset();

        assertEquals(0, restaurantSession.getCurrentCustomers().size());
        assertEquals(0, restaurantSession.getTallyTotalEarnings());
        assertEquals(0, restaurantSession.getTallyOperatingCosts());
        assertEquals(0, restaurantSession.getTallyNetProfit());
    }

    @Test
    public void getCurrentCustomersTest() {
        restaurantSession.customerEnters(customer2);
        restaurantSession.customerEnters(customer1);
        ArrayList<Customer> customers = restaurantSession.getCurrentCustomers();

        assertEquals(2, customers.size());
        assertEquals(customer2, customers.get(0));
        assertEquals(customer1, customers.get(1));
    }

    @Test
    public void getCurrentCustomersUnmodifiable() {
        ArrayList<Customer> customers = restaurantSession.getCurrentCustomers();

        customers.add(customer2);
        assertFalse(restaurantSession.getCurrentCustomers().contains(customer2));
        assertEquals(0, restaurantSession.getCurrentCustomers().size());
    }

    @Test
    public void toJsonTest() {
        JSONObject emptyRS = restaurantSession.toJson();

        assertEquals(0, emptyRS.getDouble("tallyTotalEarnings"));
        assertEquals(0, emptyRS.getDouble("tallyOperatingCosts"));
        assertEquals(0, emptyRS.getDouble("tallyNetProfit"));
        assertEquals(0, emptyRS.getJSONArray("currentCustomers").length());

        try {
            customer2.addPlate(bluePlate);
            restaurantSession.customerEnters(customer1);
            restaurantSession.customerEnters(customer2);

            JSONObject rsJson = restaurantSession.toJson();

            assertEquals(0, rsJson.getDouble("tallyTotalEarnings"));
            assertEquals(0, rsJson.getDouble("tallyOperatingCosts"));
            assertEquals(0, rsJson.getDouble("tallyNetProfit"));
            assertEquals(2, rsJson.getJSONArray("currentCustomers").length());

            restaurantSession.customerLeaves(customer1);
            restaurantSession.customerLeaves(customer2);

            rsJson = restaurantSession.toJson();

            assertEquals(22, rsJson.getDouble("tallyTotalEarnings"));
            assertEquals(10.5, rsJson.getDouble("tallyOperatingCosts"));
            assertEquals(11.5, rsJson.getDouble("tallyNetProfit"));
            assertEquals(0, rsJson.getJSONArray("currentCustomers").length());
        } catch (CustomerNotInListException e) {
            fail("customer setup incorrect");
        }
    }
}

package model;

import model.event.Event;
import model.event.EventLog;
import model.exceptions.CustomerNotInListException;
import org.json.JSONArray;
import persistence.Writable;
import org.json.JSONObject;

import java.util.ArrayList;

// represents a restaurant session with customer list and monetary information
public class RestaurantSession implements Writable {
    private ArrayList<Customer> currentCustomers;
    private double tallyTotalEarnings;
    private double tallyOperatingCosts;
    private double tallyNetProfit;

    // EFFECTS: constructs a restaurant session
    public RestaurantSession() {
        this.currentCustomers = new ArrayList<>();
        this.tallyTotalEarnings = 0;
        this.tallyOperatingCosts = 0;
        this.tallyNetProfit = 0;
    }

    // EFFECTS: constructs a restaurant session with given attributes for the purpose of duplication
    public RestaurantSession(ArrayList<Customer> currentCustomers, double tallyTotalEarnings,
                             double tallyOperatingCosts, double tallyNetProfit) {
        this.currentCustomers = currentCustomers;
        this.tallyTotalEarnings = tallyTotalEarnings;
        this.tallyOperatingCosts = tallyOperatingCosts;
        this.tallyNetProfit = tallyNetProfit;
    }

    // MODIFIES: this, EventLog
    // EFFECTS: adds a customer to the current restaurant session and logs event
    public void customerEnters(Customer customer) {
        this.currentCustomers.add(customer);
        EventLog.getInstance().logEvent(new Event("customer (name: " + customer.getName()
                + ", age: " + customer.getAge() + ") added to restaurant session"));
    }

    // MODIFIES: this, EventLog
    // EFFECTS: customer is removed from current restaurant session and their bill info is added to sessional total
    //          and logs event
    public void customerLeaves(Customer customer) throws CustomerNotInListException {
        boolean isInRestaurant = false;

        for (Customer cstmr : currentCustomers) {
            if (cstmr == customer) {
                isInRestaurant = true;
            }
        }

        if (!isInRestaurant) {
            throw new CustomerNotInListException();
        }

        this.tallyTotalEarnings += customer.billTally();
        this.tallyOperatingCosts += customer.operatingCostForCustomerTally();
        this.tallyNetProfit += customer.netProfitTally();
        currentCustomers.remove(customer);

        EventLog.getInstance().logEvent(new Event("customer (name: " + customer.getName()
                + ", age: " + customer.getAge() + ") removed from restaurant session"));
    }

    // MODIFIES: this
    // EFFECTS: resets restaurant status
    public void reset() {
        currentCustomers = new ArrayList<>();
        this.tallyTotalEarnings = 0;
        this.tallyOperatingCosts = 0;
        this.tallyNetProfit = 0;
    }

    // MODIFIES: EventLog
    // EFFECTS: returns list of current customers and logs action
    public ArrayList<Customer> getCurrentCustomers() {
        EventLog.getInstance().logEvent(new Event("list of current customers was accessed"));
        return new ArrayList<>(currentCustomers);
    }

    // getters and setters
    public double getTallyTotalEarnings() {
        return tallyTotalEarnings;
    }

    public double getTallyOperatingCosts() {
        return tallyOperatingCosts;
    }

    public double getTallyNetProfit() {
        return tallyNetProfit;
    }

    public void setTallyTotalEarnings(double tallyTotalEarnings) {
        this.tallyTotalEarnings = tallyTotalEarnings;
    }

    public void setTallyOperatingCosts(double tallyOperatingCosts) {
        this.tallyOperatingCosts = tallyOperatingCosts;
    }

    public void setTallyNetProfit(double tallyNetProfit) {
        this.tallyNetProfit = tallyNetProfit;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("currentCustomers", currentCustomersToJson());
        json.put("tallyTotalEarnings", tallyTotalEarnings);
        json.put("tallyOperatingCosts", tallyOperatingCosts);
        json.put("tallyNetProfit", tallyNetProfit);
        return json;
    }

    // EFFECTS: returns customers in restaurant as JSON array
    private JSONArray currentCustomersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Customer customer : currentCustomers) {
            jsonArray.put(customer.toJson());
        }

        return jsonArray;
    }
}

package model;

import model.event.Event;
import model.event.EventLog;
import model.exceptions.AgeInvalidException;
import org.json.JSONArray;
import persistence.Writable;
import org.json.JSONObject;

import java.util.ArrayList;

// represents a customer with a bill, age, and name
public class Customer implements Writable {
    private ArrayList<Plate> plates;
    private int age;
    private String name;

    // EFFECTS: constructs a Customer
    public Customer(int age, String name) throws AgeInvalidException {
        if (age < 0) {
            throw new AgeInvalidException();
        }
        this.age = age;
        this.name = name;
        this.plates = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("new Customer object (name: " + name + ", age: "
                + age + ") created"));
    }

    // EFFECTS: returns the customer's current total bill based on their plates taken
    public double billTally() {
        double accumulatingCost = 0;
        for (Plate plate : plates) {
            accumulatingCost += plate.getPrice();
        }
        return accumulatingCost;
    }

    // EFFECTS: returns the customer's current order-specific operating costs
    public double operatingCostForCustomerTally() {
        double accumulatingOperatingCost = 0;
        for (Plate plate : plates) {
            accumulatingOperatingCost += plate.getOperatingPrice();
        }
        return accumulatingOperatingCost;
    }

    // EFFECTS: returns the customer's total net profit relevant to plates taken
    public double netProfitTally() {
        double accumulatingTally = 0;
        for (Plate plate: plates) {
            accumulatingTally += plate.getItemProfit();
        }
        return accumulatingTally;
    }

    // MODIFIES: this
    // EFFECTS: adds a plate to the list of plates the customer has taken from the conveyor
    public void addPlate(Plate plate) {
        this.plates.add(plate);
    }

    // MODIFIES: this
    // EFFECTS: clears all plates from Customer
    public void resetPlates() {
        this.plates.clear();
    }

    // getters and setters
    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Plate> getPlates() {
        return new ArrayList<>(plates);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("plates", platesToJson());
        json.put("age", age);
        json.put("name", name);
        return json;
    }

    // EFFECTS: returns plates that customer has as JSON array
    private JSONArray platesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Plate plate : plates) {
            jsonArray.put(plate.toJson());
        }

        return jsonArray;
    }
}

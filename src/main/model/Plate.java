package model;

import model.exceptions.MoneyFormatException;
import model.exceptions.ProfitErrException;
import persistence.Writable;
import org.json.JSONObject;

// represents a food item in the form of a plate with an id, price, and operating price
public class Plate implements Writable {
    private final String id;
    private double price; // TODO: convert to cent representation
    private double operatingPrice;

    // REQUIRES: price, operatingPrice >= 0, operatingPrice <= price
    // EFFECTS: constructs a new plate
    public Plate(String id, double price, double operatingPrice) throws MoneyFormatException, ProfitErrException {

        if (price < 0 || operatingPrice < 0) {
            throw new MoneyFormatException();
        } else if (operatingPrice > price) {
            throw new ProfitErrException();
        }

        this.id = id;
        this.price = price;
        this.operatingPrice = operatingPrice;
    }

    // EFFECTS: returns the item specific net profit with operating costs in consideration
    public double getItemProfit() {
        return price - operatingPrice;
    }

    // getters and setters
    public void setPrice(double price) {
        this.price = price;
    }

    public void setOperatingPrice(double operatingPrice) {
        this.operatingPrice = operatingPrice;
    }

    public double getPrice() {
        return price;
    }

    public double getOperatingPrice() {
        return operatingPrice;
    }

    public String getId() {
        return id;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("price", price);
        json.put("operatingPrice", operatingPrice);
        return json;
    }
}

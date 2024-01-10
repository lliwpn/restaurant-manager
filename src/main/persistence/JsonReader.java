package persistence;

/*
Description: JSONReader basis
Author: CPSC210 Team / Paul Carter
Date: October 16th, 2021
URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
 */

import model.Customer;
import model.Menu;
import model.Plate;
import model.RestaurantSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.exceptions.ConstructionException;
import model.exceptions.PlateException;
import org.json.*;

// represents a reader that reads a JSON representation of restaurant information from a file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads RestaurantSession from file and returns it;
    // throws IOException if an error occurs reading data from file
    public RestaurantSession readRestaurant() throws IOException, ConstructionException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRestaurantSession(jsonObject.getJSONObject("rs"));
    }

    // EFFECTS: reads Menu from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Menu readMenu() throws IOException, ConstructionException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMenu(jsonObject.getJSONObject("m"));
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses restaurant session from JSON object and returns it
    private RestaurantSession parseRestaurantSession(JSONObject jsonObject) throws ConstructionException {
        RestaurantSession rs = new RestaurantSession();
        double tallyTotalEarnings = jsonObject.getDouble("tallyTotalEarnings");
        double tallyOperatingCosts = jsonObject.getDouble("tallyOperatingCosts");
        double tallyNetProfit = jsonObject.getDouble("tallyNetProfit");

        rs.setTallyTotalEarnings(tallyTotalEarnings);
        rs.setTallyOperatingCosts(tallyOperatingCosts);
        rs.setTallyNetProfit(tallyNetProfit);
        addCustomers(rs, jsonObject);
        return rs;
    }

    // EFFECTS: parses customer list from restaurant session
    private void addCustomers(RestaurantSession rs, JSONObject jsonObject) throws ConstructionException {
        JSONArray jsonArray = jsonObject.getJSONArray("currentCustomers");
        for (Object json: jsonArray) {
            JSONObject nextCustomer = (JSONObject) json;
            addCustomer(rs, nextCustomer);
        }
    }

    // EFFECTS: parses customer information; age, name, and the list of plates
    private void addCustomer(RestaurantSession rs, JSONObject jsonObject) throws ConstructionException {
        JSONArray jsonArray = jsonObject.getJSONArray("plates");
        int age = jsonObject.getInt("age");
        String name = jsonObject.getString("name");

        Customer c = new Customer(age, name);

        for (Object json : jsonArray) {
            JSONObject nextPlate = (JSONObject) json;
            addPlateCustomer(c, nextPlate);
        }
        rs.customerEnters(c);
    }

    // EFFECTS: parses plates for customer
    private void addPlateCustomer(Customer c, JSONObject jsonObject) throws PlateException {
        String id = jsonObject.getString("id");
        double price = jsonObject.getDouble("price");
        double operatingPrice = jsonObject.getDouble("operatingPrice");

        Plate p = new Plate(id, price, operatingPrice);
        c.addPlate(p);
    }

    // ---

    // EFFECTS: parses menu from JSON object and returns it
    private Menu parseMenu(JSONObject jsonObject) throws PlateException {
        Menu m = new Menu();
        addItems(m, jsonObject);
        return m;
    }

    // EFFECTS: parses list of items
    private void addItems(Menu m, JSONObject jsonObject) throws PlateException {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (Object json : jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            addItem(m, nextItem);
        }
    }

    // EFFECTS: parses each individual plate for menu
    private void addItem(Menu m, JSONObject jsonObject) throws PlateException {
        String id = jsonObject.getString("id");
        double price = jsonObject.getDouble("price");
        double operatingPrice = jsonObject.getDouble("operatingPrice");

        Plate p = new Plate(id, price, operatingPrice);

        m.addItem(p);
    }
}

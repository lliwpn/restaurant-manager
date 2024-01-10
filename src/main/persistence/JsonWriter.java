package persistence;

/*
Description: JSONWriter basis
Author: CPSC210 Team / Paul Carter
Date: October 16th, 2021
URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonWriter.java
 */

import model.RestaurantSession;
import model.Menu;
import org.json.JSONObject;

import java.io.*;

// represents a writer that writes JSON representation of restaurant session to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of restaurant session to file
    public void write(RestaurantSession rs, Menu m) {
        JSONObject json = new JSONObject();
        json.put("rs", rs.toJson());
        json.put("m", m.toJson());
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writers
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}

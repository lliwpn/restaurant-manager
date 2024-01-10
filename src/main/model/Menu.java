package model;

import model.exceptions.PlateNotInListException;
import org.json.JSONArray;
import persistence.Writable;
import org.json.JSONObject;

import java.util.ArrayList;

// represents a list of plates that can be ordered
public class Menu implements Writable {
    private ArrayList<Plate> items;

    // EFFECTS: constructs a new menu
    public Menu() {
        this.items = new ArrayList<>();
    }

    // EFFECTS: constructs a menu with given attributes for the purpose of duplication
    public Menu(ArrayList<Plate> items) {
        this.items = items;
    }

    // MODIFIES: this
    // EFFECTS: if item is not already included in menu, add item to menu and return success value
    public boolean addItem(Plate newItem) {
        for (Plate plate : items) {
            if (plate == newItem) {
                return false;
            }
        }
        items.add(newItem);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: attempts to remove given item from menu
    public void removeItem(Plate removedItem) throws PlateNotInListException {
        boolean toRemove = false;
        for (Plate plate : items) {
            if (plate == removedItem) {
                toRemove = true;
                break;
            }
        }
        if (toRemove) {
            items.remove(removedItem);
        } else {
            throw new PlateNotInListException();
        }
    }

    // getter
    public ArrayList<Plate> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("items", itemsToJson());
        return json;
    }

    // EFFECTS: returns items on menu as a JSON array
    private JSONArray itemsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Plate plate : items) {
            jsonArray.put(plate.toJson());
        }

        return jsonArray;
    }
}

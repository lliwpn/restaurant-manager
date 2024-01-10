package ui.buttons;

import model.Customer;
import ui.RestaurantAppGUI;

import javax.swing.*;
import java.util.ArrayList;

// represents an abstract button
abstract class Button extends JButton {
    protected RestaurantAppGUI frame;

    public Button(RestaurantAppGUI frame, String name) {
        super(name);
        this.frame = frame;
    }

    public Button(RestaurantAppGUI frame, String name, Icon icon) {
        super(name, icon);
        this.frame = frame;
    }

    // EFFECTS: adds a listener to the button
    protected abstract void addListener();

    // EFFECTS: returns a list of customer names, used for customer editing
    protected ArrayList<String> customerNames(ArrayList<Customer> customers) {
        ArrayList<String> names = new ArrayList<>();
        for (Customer customer : customers) {
            names.add(customer.getName());
        }
        return names;
    }

    // EFFECTS: converts an arraylist of type string to a string array
    protected String[] toStrArray(ArrayList<String> names) {
        return names.toArray(new String[names.size()]);
    }
}

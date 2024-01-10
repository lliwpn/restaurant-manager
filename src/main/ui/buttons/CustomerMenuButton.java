package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a customer menu button
public class CustomerMenuButton extends Button {

    // EFFECTS: constructs a customer menu button
    public CustomerMenuButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new CustomerMenuButtonListener());
    }

    private class CustomerMenuButtonListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes button layout to customer menu buttons when pressed
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.toCustomerMenu();
        }

    }
}

package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a customer information button
public class CustomerInfoButton extends Button {

    // EFFECTS: constructs a customer info button
    public CustomerInfoButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new CustomerInfoButtonListener());
    }

    private class CustomerInfoButtonListener implements ActionListener {

        // EFFECTS: displays current customer information
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.displayCustomerInfo();
        }

    }
}

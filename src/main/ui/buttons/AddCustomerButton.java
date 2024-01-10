package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents an add customer button
public class AddCustomerButton extends Button {

    // EFFECTS: constructs an adding customer to session button
    public AddCustomerButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new AddCustomerButtonListener());
    }

    private class AddCustomerButtonListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: takes user input for menu item addition and checks for input validity
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("what's the customer's name?");
            if (name == null) {
                return;
            }
            String age = JOptionPane.showInputDialog("what's the customer's age?");
            if (age == null) {
                return;
            }

            try {
                int numAge = Integer.parseInt(age);
                frame.addCustomer(name, numAge);
            } catch (Exception c) {
                JOptionPane.showMessageDialog(frame, "error: the customer age was an invalid value");
            }
        }

    }
}

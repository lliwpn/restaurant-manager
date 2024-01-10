package ui.buttons;

import model.Customer;
import model.RestaurantSession;
import model.exceptions.CustomerNotInListException;
import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// represents a remove customer button
public class RemoveCustomerButton extends Button {

    // EFFECTS: constructs a remove customer button
    public RemoveCustomerButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new RemoveCustomerButtonListener());
    }

    private class RemoveCustomerButtonListener implements ActionListener {
        private ArrayList<Customer> customers;

        // MODIFIES: this
        // EFFECTS: removes customer from session
        @Override
        public void actionPerformed(ActionEvent e) {
            RestaurantSession rs = frame.getRestaurantSession();
            customers = rs.getCurrentCustomers();
            String[] names = toStrArray(customerNames(customers));

            if (customers.size() == 0) {
                JOptionPane.showMessageDialog(frame, "it looks like there aren't any customers yet");
            } else {
                JComboBox nameList = new JComboBox(names);
                nameList.addActionListener(new NameListListener());
                JOptionPane.showMessageDialog(frame, nameList);
            }
        }

        private class NameListListener implements ActionListener {

            // EFFECTS: processes user input and attempts to remove customer from list
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Customer selectedCustomer = customers.get(comboBox.getSelectedIndex());

                try {
                    frame.removeCustomer(selectedCustomer);
                } catch (CustomerNotInListException c) {
                    JOptionPane.showMessageDialog(frame, "error: customer could not be removed");
                }
            }
        }
    }
}

package ui.buttons;

import model.Customer;
import model.Menu;
import model.Plate;
import model.RestaurantSession;
import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// represents a customer order button
public class CustomerOrderButton extends Button {

    // EFFECTS: constructs a customer order button
    public CustomerOrderButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    public void addListener() {
        addActionListener(new CustomerOrderButtonListener());
    }

    private class CustomerOrderButtonListener implements ActionListener {
        private ArrayList<Customer> customers;

        // MODIFIES: this
        // EFFECTS: opens up menu of customers to direct to for further processing
        @Override
        public void actionPerformed(ActionEvent e) {
            RestaurantSession rs = frame.getRestaurantSession();
            customers = rs.getCurrentCustomers();
            String[] rsNames = toStrArray(customerNames(customers));

            if (customers.size() == 0) {
                JOptionPane.showMessageDialog(frame, "it looks like there aren't any customers yet");
            } else {
                JComboBox nameList = new JComboBox(rsNames);
                nameList.addActionListener(new NameListListener());
                JOptionPane.showMessageDialog(frame, nameList);
            }
        }

        // EFFECTS: returns a list of menu item names
        private ArrayList<String> menuNames(ArrayList<Plate> plates) {
            ArrayList<String> names = new ArrayList<>();
            for (Plate plate : plates) {
                names.add(plate.getId());
            }
            return names;
        }

        private class NameListListener implements ActionListener {
            private ArrayList<Plate> menuItems;
            private Customer selectedCustomer;

            // MODIFIES: this
            // EFFECTS: attempts to prompt user to edit selected customer order
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu menu = frame.getMenu();
                menuItems = menu.getItems();
                String[] menuIds = toStrArray(menuNames(menuItems));

                JComboBox comboBox = (JComboBox) e.getSource();
                selectedCustomer = customers.get(comboBox.getSelectedIndex());

                if (menuItems.size() == 0) {
                    JOptionPane.showMessageDialog(frame, "it looks like there's nothing on the menu to add yet");
                } else {
                    JComboBox menuNameList = new JComboBox(menuIds);
                    menuNameList.addActionListener(new MenuNameListListener());
                    JOptionPane.showMessageDialog(frame, menuNameList);
                }
            }

            private class MenuNameListListener implements ActionListener {

                // EFFECTS: attempts to add item to customer bill
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox comboBox = (JComboBox) e.getSource();
                    Plate selectedPlate = menuItems.get(comboBox.getSelectedIndex());

                    frame.addOrder(selectedCustomer, selectedPlate);
                    JOptionPane.showMessageDialog(frame, "order updated!");
                }
            }
        }
    }
}

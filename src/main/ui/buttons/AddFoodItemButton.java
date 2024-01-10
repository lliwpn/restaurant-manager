package ui.buttons;

import model.exceptions.PlateException;
import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents an adding food item to menu button
public class AddFoodItemButton extends Button {

    // EFFECTS: constructs an adding item to menu button
    public AddFoodItemButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new AddFoodItemButtonListener());
    }

    private class AddFoodItemButtonListener implements ActionListener {

        // EFFECTS: takes user input for food menu item and attempts to add to menu
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("what's the item's name?");
            if (name == null) {
                return;
            }
            String inputPrice = JOptionPane.showInputDialog("how much is it priced as?");
            if (inputPrice == null) {
                return;
            }
            String inputOperatingPrice = JOptionPane.showInputDialog("how much does it cost to produce?");
            if (inputOperatingPrice == null) {
                return;
            }

            try {
                double price = Double.parseDouble(inputPrice);
                double operatingPrice = Double.parseDouble(inputOperatingPrice);
                frame.addMenuItem(name, price, operatingPrice);
            } catch (NumberFormatException c) {
                JOptionPane.showMessageDialog(frame, "error: the monetary input was not formatted correctly");
            } catch (PlateException c) {
                JOptionPane.showMessageDialog(frame, "error: the monetary input was invalid");
            }
        }
    }
}

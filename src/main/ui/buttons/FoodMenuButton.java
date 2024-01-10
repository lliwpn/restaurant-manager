package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a food menu button
public class FoodMenuButton extends Button {

    // EFFECTS: constructs a food menu button
    public FoodMenuButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new FoodMenuButtonListener());
    }

    private class FoodMenuButtonListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes button layout to food menu layout when pressed
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.toFoodMenu();
        }

    }
}

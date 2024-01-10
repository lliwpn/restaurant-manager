package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a menu information button
public class MenuInfoButton extends Button {

    // EFFECTS: constructs a menu information button
    public MenuInfoButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new MenuInfoButtonListener());
    }

    private class MenuInfoButtonListener implements ActionListener {

        // EFFECTS: displays menu information when button is pressed
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.displayFoodInfo();
        }
    }
}

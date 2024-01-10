package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a restaurant information button
public class StoreInfoButton extends Button {

    // EFFECTS: constructs a restaurant information button
    public StoreInfoButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new StoreInfoButtonListener());
    }

    private class StoreInfoButtonListener implements ActionListener {

        // EFFECTS: displays restaurant session information in a popup window
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.displayRSInfo();
        }
    }
}

package ui.buttons;

import ui.RestaurantAppGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a button that returns to the initial menu
public class GoBackButton extends Button {

    // EFFECTS: constructs a return button
    public GoBackButton(RestaurantAppGUI frame, String name) {
        super(frame, name);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    public void addListener() {
        addActionListener(new GoBackButtonListener());
    }

    private class GoBackButtonListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes button layout to main menu layout when pressed
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.toMainMenu();
        }

    }
}

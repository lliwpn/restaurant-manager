package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// represents a reset button
public class ResetButton extends Button {

    // EFFECTS: constructs a reset button
    public ResetButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding JButton
    @Override
    protected void addListener() {
        addActionListener(new ResetButtonHandler());
    }

    private class ResetButtonHandler implements ActionListener {

        // MODIFIES: this
        // EFFECTS: resets restaurant session when button is pressed and displays confirmation popup
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.resetSession();
            JOptionPane.showMessageDialog(frame, "session reset!");
        }
    }
}

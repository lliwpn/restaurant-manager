package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

// represents a save button
public class SaveButton extends Button {

    // EFFECTS: constructs a save button
    public SaveButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to corresponding button
    @Override
    protected void addListener() {
        addActionListener(new SaveButtonListener());
    }

    private class SaveButtonListener implements ActionListener {

        // EFFECTS: saves data to established path when button is pressed
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                frame.saveData();
                JOptionPane.showMessageDialog(frame, "session and menu were saved!");
            } catch (FileNotFoundException c) {
                JOptionPane.showMessageDialog(frame, "error: unable to save file to given path");
            }
        }
    }
}

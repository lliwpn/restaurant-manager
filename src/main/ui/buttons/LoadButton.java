package ui.buttons;

import ui.RestaurantAppGUI;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// represents a load button
public class LoadButton extends Button {

    // EFFECTS: constructs a load button
    public LoadButton(RestaurantAppGUI frame, String name, Icon icon) {
        super(frame, name, icon);
        addListener();
    }

    // MODIFIES: this
    // EFFECTS: adds an ActionListener to the corresponding button
    @Override
    protected void addListener() {
        addActionListener(new LoadButtonListener());
    }

    private class LoadButtonListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: attempts to load restaurant and menu information from given path
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                frame.loadData();
                JOptionPane.showMessageDialog(frame, "session and menu were loaded successfully!");
            } catch (Exception c) {
                JOptionPane.showMessageDialog(frame, "error: unable to read from file");
            }
        }

    }
}

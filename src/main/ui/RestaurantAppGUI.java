package ui;

import model.Customer;
import model.Menu;
import model.Plate;
import model.RestaurantSession;
import model.event.Event;
import model.event.EventLog;
import model.exceptions.AgeInvalidException;
import model.exceptions.ConstructionException;
import model.exceptions.CustomerNotInListException;
import model.exceptions.PlateException;
import persistence.JsonWriter;
import persistence.JsonReader;
import ui.buttons.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

// represents a visual restaurant application
public class RestaurantAppGUI extends JFrame {
    public static final int WIDTH = 420;
    public static final int HEIGHT = 280;
    public static final int BUTTON_WINDOW_WIDTH = 360;
    public static final int INFO_WINDOW_WIDTH = WIDTH - BUTTON_WINDOW_WIDTH;
    public static final String JSON_STORE = "./data/restaurant.json";
    public static final String ICON_STORE = "./data/sushi.png";
    public static final Font HEADER_FONT = new Font("Courier New", Font.PLAIN, 18);
    public static final Font SUB_FONT = new Font("Courier", Font.PLAIN, 9);
    public static final Font DEFAULT_FONT = new Font("Courier New", Font.PLAIN, 14);

    private final DecimalFormat df = new DecimalFormat("0.00");

    private RestaurantSession rs;
    private Menu menu;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanel buttonPanel;
    private JPanel infoPanel;
    private GridLayout buttonLayout;
    private ArrayList<JButton> mainMenuButtons;
    private ArrayList<JButton> customerMenuButtons;
    private ArrayList<JButton> foodMenuButtons;

    private boolean rsInfoIsShowing = false;
    private boolean customerInfoIsShowing = false;
    private boolean foodInfoIsShowing = false;

    // EFFECTS: constructs restaurant GUI object
    public RestaurantAppGUI() {
        super("restaurant manager");
        initializeFields();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS: initializes restaurant relevant fields and persistence
    private void initializeFields() {
        rs = new RestaurantSession();
        menu = new Menu();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        addWindowListener(new MainWindowListener());
        initializeButtons();
    }

    // EFFECTS: initializes sets of buttons
    private void initializeButtons() {
        initializeMainMenuButtons();
        initializeCustomerMenuButtons();
        initializeFoodMenuButtons();
    }

    // MODIFIES: this
    // EFFECTS: initializes main menu buttons
    private void initializeMainMenuButtons() {
        ImageIcon icon1 = new ImageIcon("./data/reset.png");
        ImageIcon icon2 = new ImageIcon("./data/monetary.png");
        ImageIcon icon3 = new ImageIcon("./data/customer.png");
        ImageIcon icon4 = new ImageIcon("./data/menu.png");
        ImageIcon icon5 = new ImageIcon("./data/customermenu.png");
        ImageIcon icon6 = new ImageIcon("./data/addtomenu.png");
        ImageIcon icon7 = new ImageIcon("./data/save.png");
        ImageIcon icon8 = new ImageIcon("./data/load.png");

        mainMenuButtons = new ArrayList<>();
        mainMenuButtons.add(new ResetButton(this, "reset session", icon1));
        mainMenuButtons.add(new StoreInfoButton(this, "restaurant info", icon2));
        mainMenuButtons.add(new CustomerInfoButton(this, "customer info", icon3));
        mainMenuButtons.add(new MenuInfoButton(this, "menu info", icon4));
        mainMenuButtons.add(new CustomerMenuButton(this, "edit customers", icon5));
        mainMenuButtons.add(new FoodMenuButton(this, "add to menu", icon6));
        mainMenuButtons.add(new SaveButton(this, "save data", icon7));
        mainMenuButtons.add(new LoadButton(this, "load data", icon8));
    }

    // MODIFIES: this
    // EFFECTS: initializes customer menu buttons
    private void initializeCustomerMenuButtons() {
        ImageIcon icon1 = new ImageIcon("./data/addcustomer.png");
        ImageIcon icon2 = new ImageIcon("./data/removecustomer.png");
        ImageIcon icon3 = new ImageIcon("./data/addtomenu.png");

        customerMenuButtons = new ArrayList<>();
        customerMenuButtons.add(new AddCustomerButton(this, "add customer", icon1));
        customerMenuButtons.add(new RemoveCustomerButton(this, "remove customer", icon2));
        customerMenuButtons.add(new CustomerOrderButton(this, "add to order", icon3));
        customerMenuButtons.add(new GoBackButton(this, "go back"));
    }

    // MODIFIES: this
    // EFFECTS: initializes food menu buttons
    private void initializeFoodMenuButtons() {
        ImageIcon icon1 = new ImageIcon("./data/addtomenu.png");

        foodMenuButtons = new ArrayList<>();
        foodMenuButtons.add(new AddFoodItemButton(this, "add item", icon1));
        foodMenuButtons.add(new GoBackButton(this, "go back"));
    }

    // MODIFIES: this
    // EFFECTS: draws JFrame window where DrawingEditor will operate and prompts menu construction
    private void initializeGraphics() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        ImageIcon ii = new ImageIcon(ICON_STORE);
        setIconImage(ii.getImage());
        initializePanels();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes button and info panels
    private void initializePanels() {
        JPanel textPanel = new JPanel();
        infoPanel = new JPanel();
        buttonPanel = new JPanel();

        setLayout(new GridLayout(1, 3));

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.setSize(new Dimension(INFO_WINDOW_WIDTH, HEIGHT));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        buttonLayout = new GridLayout();
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.setSize(new Dimension(BUTTON_WINDOW_WIDTH, HEIGHT));

        updateDisplayButtons(mainMenuButtons);
        textPanel.add(Box.createRigidArea(new Dimension(3, 0)));
        textPanel.add(infoPanel);
        add(buttonPanel);
        add(textPanel);
    }

    // MODIFIES: this
    // EFFECTS: updates current displayed buttons in JFrame according to given list
    private void updateDisplayButtons(ArrayList<JButton> buttons) {
        buttonPanel.removeAll();
        buttonLayout.setRows(buttons.size() / 2);
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        buttonPanel.repaint();
        buttonPanel.revalidate();
    }

    // MODIFIES: this
    // EFFECTS: displays restaurant information to infoPanel
    public void displayRSInfo() {
        updateMenuVisibilityFlags(0);

        infoPanel.removeAll();
        JLabel label1 = new JLabel("current totals:");
        label1.setFont(HEADER_FONT);
        JLabel label2 = new JLabel("(customers must leave before their bill is added to consideration)");
        label2.setFont(SUB_FONT);
        JLabel label3 = new JLabel("total earnings: $" + df.format(rs.getTallyTotalEarnings()));
        label3.setFont(DEFAULT_FONT);
        JLabel label4 = new JLabel("total operating costs: $" + df.format(rs.getTallyOperatingCosts()));
        label4.setFont(DEFAULT_FONT);
        JLabel label5 = new JLabel("net profit: $" + df.format(rs.getTallyNetProfit()));
        label5.setFont(DEFAULT_FONT);

        infoPanel.add(label1);
        infoPanel.add(Box.createRigidArea(new Dimension(0,2)));
        infoPanel.add(label2);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(label3);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 1)));
        infoPanel.add(label4);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 1)));
        infoPanel.add(label5);

        finalizeInfoPanel();
    }

    // MODIFIES: this
    // EFFECTS: displays customer information to infoPanel
    public void displayCustomerInfo() {
        int counter = 1;

        updateMenuVisibilityFlags(1);

        infoPanel.removeAll();
        JLabel label1 = new JLabel("current customers:");
        label1.setFont(HEADER_FONT);
        infoPanel.add(label1);
        for (Customer customer : rs.getCurrentCustomers()) {
            JLabel nameLabel = new JLabel(counter + ") " + customer.getName());
            nameLabel.setFont(DEFAULT_FONT);
            JLabel ageLabel = new JLabel("age: " + customer.getAge());
            ageLabel.setFont(DEFAULT_FONT);
            JLabel billLabel = new JLabel("current bill: $" + df.format(customer.billTally()));
            billLabel.setFont(DEFAULT_FONT);

            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            infoPanel.add(ageLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            infoPanel.add(billLabel);
            counter++;
        }

        finalizeInfoPanel();
    }

    // MODIFIES: this
    // EFFECTS: displays food menu item information
    public void displayFoodInfo() {
        updateMenuVisibilityFlags(2);

        infoPanel.removeAll();
        updateFoodLabels();

        finalizeInfoPanel();
    }

    // MODIFIES: this
    // EFFECTS: initializes food menu JLabels and updates infoPanel
    private void updateFoodLabels() {
        int counter = 1;

        JLabel label1 = new JLabel("menu information:");
        label1.setFont(HEADER_FONT);
        infoPanel.add(label1);
        for (Plate plate : menu.getItems()) {
            JLabel nameLabel = new JLabel(counter + ") " + plate.getId());
            nameLabel.setFont(DEFAULT_FONT);
            JLabel priceLabel = new JLabel("price: $" + plate.getPrice());
            priceLabel.setFont(DEFAULT_FONT);
            JLabel operatingLabel = new JLabel("production cost: $" + plate.getOperatingPrice());
            operatingLabel.setFont(DEFAULT_FONT);
            JLabel profitLabel = new JLabel("item net profit: $" + plate.getItemProfit());
            profitLabel.setFont(DEFAULT_FONT);

            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            infoPanel.add(priceLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            infoPanel.add(operatingLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            infoPanel.add(profitLabel);

            counter++;
        }
    }

    // MODIFIES: this
    // EFFECTS: updates visibility flags based on given input
    private void updateMenuVisibilityFlags(int flag) {
        if (flag == 0) {
            rsInfoIsShowing = true;
            customerInfoIsShowing = false;
            foodInfoIsShowing = false;
        } else if (flag == 1) {
            rsInfoIsShowing = false;
            customerInfoIsShowing = true;
            foodInfoIsShowing = false;
        } else if (flag == 2) {
            rsInfoIsShowing = false;
            customerInfoIsShowing = false;
            foodInfoIsShowing = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds fish gif to gui and revalidates infoPanel
    private void finalizeInfoPanel() {
        ImageIcon fish = new ImageIcon("./data/fish.gif");
        JLabel fishLabel = new JLabel(fish);
        infoPanel.add(fishLabel);

        infoPanel.repaint();
        infoPanel.revalidate();
    }

    // MODIFIES: this
    // EFFECTS: attempts to add a customer to the restaurant session
    public void addCustomer(String name, int age) throws AgeInvalidException {
        rs.customerEnters(new Customer(age, name));
        if (customerInfoIsShowing) {
            displayCustomerInfo();
        }
    }

    // MODIFIES: this
    // EFFECTS: attempts to remove customer from the restaurant session
    public void removeCustomer(Customer customer) throws CustomerNotInListException {
        rs.customerLeaves(customer);
        if (rsInfoIsShowing) {
            displayRSInfo();
        } else if (customerInfoIsShowing) {
            displayCustomerInfo();
        }
    }

    // MODIFIES: this
    // EFFECTS: attempts to add plate to customer bill
    public void addOrder(Customer customer, Plate plate) {
        customer.addPlate(plate);
        if (customerInfoIsShowing) {
            displayCustomerInfo();
        }
    }

    // MODIFIES: this
    // EFFECTS: attempts to add an item to the menu
    public void addMenuItem(String name, double price, double operatingPrice) throws PlateException {
        menu.addItem(new Plate(name, price, operatingPrice));
        if (foodInfoIsShowing) {
            displayFoodInfo();
        }
    }

    // EFFECTS: saves restaurant session and menu to file
    public void saveData() throws FileNotFoundException {
        jsonWriter.open();
        jsonWriter.write(rs, menu);
        jsonWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: loads restaurant session and menu from file
    public void loadData() throws IOException, ConstructionException {
        rs = jsonReader.readRestaurant();
        menu = jsonReader.readMenu();
        if (rsInfoIsShowing) {
            displayRSInfo();
        } else if (customerInfoIsShowing) {
            displayCustomerInfo();
        } else if (foodInfoIsShowing) {
            displayFoodInfo();
        }
    }

    // MODIFIES: this
    // EFFECTS: changes GUI components to main menu
    public void toMainMenu() {
        updateDisplayButtons(mainMenuButtons);
    }

    // MODIFIES: this
    // EFFECTS: changes GUI components to customer menu
    public void toCustomerMenu() {
        updateDisplayButtons(customerMenuButtons);
    }

    // MODIFIES: this
    // EFFECTS: changes GUI components to food menu
    public void toFoodMenu() {
        updateDisplayButtons(foodMenuButtons);
    }

    // MODIFIES: this
    // EFFECTS: resets restaurant session
    public void resetSession() {
        rs.reset();
        if (rsInfoIsShowing) {
            displayRSInfo();
        } else if (customerInfoIsShowing) {
            displayCustomerInfo();
        }
    }

    // getters
    public RestaurantSession getRestaurantSession() {
        return new RestaurantSession(rs.getCurrentCustomers(), rs.getTallyTotalEarnings(),
                rs.getTallyOperatingCosts(), rs.getTallyNetProfit());
    }

    public Menu getMenu() {
        return new Menu(menu.getItems());
    }

    private static class MainWindowListener implements WindowListener {

        public void windowOpened(WindowEvent e) {

        }

        // EFFECTS: when GUI window is closed, print event log to console
        @Override
        public void windowClosing(WindowEvent e) {
            for (Event event : EventLog.getInstance()) {
                System.out.println(event.toString());
            }
        }

        public void windowClosed(WindowEvent e) {

        }

        public void windowIconified(WindowEvent e) {

        }

        public void windowDeiconified(WindowEvent e) {

        }

        public void windowActivated(WindowEvent e) {

        }

        public void windowDeactivated(WindowEvent e) {

        }
    }
}

package ui;

/*
Description: TellerApp used as reference
Author: CPSC210 Team / sam
Date: July 26th, 2021
URL: https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/main/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
 */

import model.Customer;
import model.Menu;
import model.Plate;
import model.RestaurantSession;
import model.exceptions.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// represents the restaurant application
public class RestaurantApp {
    private static final String JSON_STORE = "./data/restaurant.json";
    private Scanner input;
    private RestaurantSession restaurantSession;
    private Menu menu;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the restaurant application
    public RestaurantApp() {
        runRestaurant();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runRestaurant() {
        boolean stillGoing = true;
        String command;

        init();

        System.out.println(" -~- Welcome to the Conveyor Belt Restaurant Manager! -~-");
        while (stillGoing) {
            displayCmdMenu();
            command = input.next().toLowerCase();

            if (command.equals("9")) {
                System.out.println("See you later!");
                stillGoing = false;
            } else {
                processCmd(command);
            }
        }
    }

    // EFFECTS: processes command input
    private void processCmd(String cmd) {
        if (cmd.equals("1")) {
            doReset();
        } else if (cmd.equals("2")) {
            viewCustomerInfo();
        } else if (cmd.equals("3")) {
            viewMonetaryInfo();
        } else if (cmd.equals("4")) {
            viewMenuInfo();
        } else if (cmd.equals("5")) {
            editCustomer();
        } else if (cmd.equals("6")) {
            editMenu();
        } else if (cmd.equals("7")) {
            saveData();
        } else if (cmd.equals("8")) {
            loadData();
        } else {
            System.out.println("Oops! Your input is invalid - try again.");
        }
    }

    // EFFECTS: saves restaurant session and menu to file
    private void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(restaurantSession, menu);
            jsonWriter.close();
            System.out.println("Saved session to " + JSON_STORE + "!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads restaurant session and menu from file
    private void loadData() {
        try {
            restaurantSession = jsonReader.readRestaurant();
            menu = jsonReader.readMenu();
            System.out.println("Loaded successfully from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (ConstructionException e) {
            System.out.println("Oops! There was a parsing error.");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes scanner, restaurant session, menu, and json objects
    private void init() {
        restaurantSession = new RestaurantSession();
        menu = new Menu();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays command menu
    private void displayCmdMenu() {
        System.out.println("\n(1) : reset session");
        System.out.println("(2) : view current customer information");
        System.out.println("(3) : view store total profit/operating costs/net profit");
        System.out.println("(4) : view current menu information");
        System.out.println("(5) : edit current customer information (ordering items, logging entries/departures)");
        System.out.println("(6) : edit menu information (add items)");
        System.out.println("(7) : save restaurant session and menu");
        System.out.println("(8) : load restaurant session and menu");
        System.out.println("(9) : exit\n");
    }

    // MODIFIES: this
    // EFFECTS: resets restaurant session and displays final sessional monetary totals
    private void doReset() {
        viewMonetaryInfo();
        System.out.println("Session has successfully reset.");
        restaurantSession = new RestaurantSession();
    }

    // EFFECTS: displays all current customer information (order value, personal info)
    private void viewCustomerInfo() {
        ArrayList<Customer> customers = restaurantSession.getCurrentCustomers();
        int counter = 0;

        if (customers.size() == 0) {
            System.out.println("It looks like there are no customers to view at this time.");
        } else {
            for (Customer customer : customers) {
                System.out.println((counter + 1) + ") " + customer.getName());
                System.out.println("Age: " + customer.getAge());
                System.out.printf("Current bill: $%.2f\n", customer.billTally());
                counter++;
            }
        }
    }

    // EFFECTS: displays all current monetary information (total profit, operating costs, net profit)
    private void viewMonetaryInfo() {
        System.out.println("Current totals (note: customers must leave before their bill is added to consideration):");
        System.out.printf("Total profit: $%.2f\n", restaurantSession.getTallyTotalEarnings());
        System.out.printf("Total operating costs: $%.2f\n", restaurantSession.getTallyOperatingCosts());
        System.out.printf("Net profit: $%.2f\n", restaurantSession.getTallyNetProfit());
    }

    // EFFECTS: displays menu item information (price, operating cost, net item profit, plate info)
    private void viewMenuInfo() {
        ArrayList<Plate> items = menu.getItems();
        int counter = 0;

        if (items.size() == 0) {
            System.out.println("It looks like there are no menu items to view at this time.");
        } else {
            for (Plate plate : items) {
                System.out.println((counter + 1) + ") " + plate.getId());
                System.out.printf("Price: $%.2f\n", plate.getPrice());
                System.out.printf("Operating cost per unit: $%.2f\n", plate.getOperatingPrice());
                System.out.printf("Net profit per unit: $%.2f\n", plate.getItemProfit());
                counter++;
            }
        }
    }

    // EFFECTS: displays customer information and prompts information entry
    private void editCustomer() {
        boolean stillGoing = true;
        String command;

        while (stillGoing) {
            displayCustomerMenu();
            command = input.next().toLowerCase();

            if (command.equals("4")) {
                stillGoing = false;
            } else {
                processCustomerCmd(command);
            }
        }
    }

    // EFFECTS: displays menu information and prompts menu editing for food menu
    private void editMenu() {
        boolean stillGoing = true;
        String command;

        viewMenuInfo();
        while (stillGoing) {
            displayMenuMenu();
            command = input.next().toLowerCase();

            if (command.equals("2")) {
                stillGoing = false;
            } else {
                processMenuCmd(command);
            }
        }
    }

    // EFFECTS: displays food item menu menu
    private void displayMenuMenu() {
        System.out.println("\n(1) : add item");
        System.out.println("(2) : nevermind, i want to do something else\n");
    }

    // EFFECTS: processes user command on food item menu menu
    private void processMenuCmd(String cmd) {
        if (cmd.equals("1")) {
            doAddItem();
        } else {
            System.out.println("Oops! Your input is invalid - try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds item to menu
    private void doAddItem() {
        String plateId;
        double price;
        double operatingCost;

        try {
            System.out.println("What's the name of the item to be added?");
            plateId = input.next();
            System.out.println("What's the price of one " + plateId + "?");
            price = Double.parseDouble(input.next());
            System.out.println("What are the costs of producing one " + plateId + "?");
            operatingCost = Double.parseDouble(input.next());
            menu.addItem(new Plate(plateId, price, operatingCost));
            System.out.println("Success!");
        } catch (NumberFormatException e) {
            System.out.println("Oops! That wasn't a valid input - try again.");
        } catch (MoneyFormatException e) {
            System.out.println("Oops! That isn't a proper monetary value - try again.");
        } catch (ProfitErrException e) {
            System.out.println("Oops! Your item was more expensive to make than the profit it was earning - try again");
        }
    }

    // EFFECTS: displays customer command menu
    private void displayCustomerMenu() {
        System.out.println("\n(1) : add a new customer to the session");
        System.out.println("(2) : add to a customer's order");
        System.out.println("(3) : remove a customer from the session");
        System.out.println("(4) : nevermind, i want to do something else\n");
    }

    // EFFECTS: processes user command on customer menu
    private void processCustomerCmd(String cmd) {
        if (cmd.equals("1")) {
            addCustomer();
        } else if (cmd.equals("2")) {
            addToCustomerOrder();
        } else if (cmd.equals("3")) {
            removeCustomer();
        } else {
            System.out.println("Oops! Your input is invalid - try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a customer to the current restaurant session
    private void addCustomer() {
        String name;
        int age;

        System.out.println("What is the customer's name?");
        name = input.next();
        System.out.println("What is the customer's age?");
        try {
            age = Integer.parseInt(input.next());
            restaurantSession.customerEnters(new Customer(age, name));
            System.out.println("Customer added!");
        } catch (NumberFormatException e) {
            System.out.println("Oops! Your age input didn't seem to be a number.");
        } catch (AgeInvalidException e) {
            System.out.println("Oops! The customer's age didn't seem to be right.");
        }
    }

    // EFFECTS: adds plate(s) to a customer's order
    private void addToCustomerOrder() {
        int id;
        ArrayList<Customer> customers = restaurantSession.getCurrentCustomers();
        Customer customerToEdit;

        if (customers.size() == 0) {
            System.out.println("There are no customers to add to the order of.");
        } else {
            viewCustomerInfo();
            System.out.println("Which customer is ordering? (please use the provided numbers)");
            try {
                id = Integer.parseInt(input.next());
                if (id > customers.size() || id < 1) {
                    System.out.println("Oops! This number doesn't seem to match the list.");
                } else {
                    customerToEdit = customers.get(id - 1);
                    choosePlate(customerToEdit);
                }
            } catch (NumberFormatException e) {
                System.out.println("Oops! Your age input didn't seem to be a number.");
            }
        }
    }

    // EFFECTS: input verification loop for plate choice
    public void choosePlate(Customer customer) {
        boolean stillGoing = true;
        String command;
        ArrayList<Plate> plates = menu.getItems();

        if (plates.size() == 0) {
            System.out.println("There are no items on the menu to add.");
        } else {
            viewMenuInfo();
            while (stillGoing) {
                addPlate(customer);
                System.out.println("Order finished? (n for no)");
                command = input.next().toLowerCase();
                if (!command.equals("n")) {
                    stillGoing = false;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds plate to customer
    public void addPlate(Customer customer) {
        int id;
        ArrayList<Plate> plates = menu.getItems();

        System.out.println("Which item is the customer ordering? (please use the provided numbers)");
        try {
            id = Integer.parseInt(input.next());
            if (id > plates.size() || id < 1) {
                System.out.println("Oops! Your input was invalid.");
            } else {
                customer.addPlate(plates.get(id - 1));
            }
        } catch (NumberFormatException e) {
            System.out.println("Oops! Your input was invalid.");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes a customer from the current restaurant session
    private void removeCustomer() {
        int id;
        ArrayList<Customer> customers = restaurantSession.getCurrentCustomers();
        Customer customerToRemove;

        if (customers.size() == 0) {
            System.out.println("There are no customers to remove.");
        } else {
            viewCustomerInfo();
            System.out.println("Which customer is leaving? (please use the provided numbers)");
            try {
                id = Integer.parseInt(input.next());
                if (id > customers.size() || id < 1) {
                    System.out.println("Oops! This number doesn't seem to match the list.");
                } else {
                    customerToRemove = customers.get(id - 1);
                    restaurantSession.customerLeaves(customerToRemove);
                    System.out.println("Customer removed!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Oops! Your age input didn't seem to be a number.");
            } catch (CustomerNotInListException e) {
                System.out.println("This command is broken.");
            }
        }
    }
}

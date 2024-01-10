package model;

import model.exceptions.MoneyFormatException;
import model.exceptions.PlateNotInListException;
import model.exceptions.ProfitErrException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    Menu menu;
    Plate redPlate;
    Plate greenPlate;
    Plate bluePlate;

    @BeforeEach
    public void setup() {
        menu = new Menu();

        try {
            redPlate = new Plate("red", 5.00, 2.50);
            greenPlate = new Plate("green", 2.50, 1.50);
            bluePlate = new Plate("blue", 0, 0);
        } catch (MoneyFormatException e) {
            fail("money format error");
        } catch (ProfitErrException e) {
            fail("profit (operatingcost > price) error");
        }
    }

    @Test
    public void duplicateConsTest() {
        ArrayList<Plate> plates = new ArrayList<>();
        plates.add(redPlate);
        plates.add(greenPlate);
        plates.add(bluePlate);
        Menu duplicateMenu = new Menu(plates);

        assertEquals(3, duplicateMenu.getItems().size());
    }

    @Test
    public void unmodifiableGetPlatesTest() {
        ArrayList<Plate> plates = menu.getItems();

        plates.add(redPlate);
        assertFalse(menu.getItems().contains(redPlate));
        assertEquals(0, menu.getItems().size());
    }

    @Test
    public void addItemNotPreExisting() {
        assertTrue(menu.addItem(redPlate));
        ArrayList<Plate> items = menu.getItems();
        assertEquals(1, items.size());
        assertEquals(redPlate, items.get(0));
    }

    @Test
    public void addItemPreExisting() {
        assertTrue(menu.addItem(bluePlate));
        ArrayList<Plate> items = menu.getItems();
        assertEquals(1, items.size());
        assertEquals(bluePlate, items.get(0));

        assertFalse(menu.addItem(bluePlate));
        items = menu.getItems();
        assertEquals(1, items.size());
        assertEquals(bluePlate, items.get(0));
    }

    @Test
    public void addMultipleItems() {
        assertTrue(menu.addItem(redPlate));
        ArrayList<Plate> items = menu.getItems();
        assertEquals(1, items.size());
        assertEquals(redPlate, items.get(0));

        assertTrue(menu.addItem(bluePlate));
        items = menu.getItems();
        assertEquals(2, items.size());
        assertEquals(redPlate, items.get(0));
        assertEquals(bluePlate, items.get(1));

        assertTrue(menu.addItem(greenPlate));
        items = menu.getItems();
        assertEquals(3, items.size());
        assertEquals(redPlate, items.get(0));
        assertEquals(bluePlate, items.get(1));
        assertEquals(greenPlate, items.get(2));
    }

    @Test
    public void removeItemNotPreExisting() {
        menu.addItem(greenPlate);
        menu.addItem(bluePlate);
        ArrayList<Plate> items = menu.getItems();
        assertEquals(2, items.size());

        try {
            menu.removeItem(redPlate);
            fail("didn't throw");
        } catch (PlateNotInListException e) {
            // success
        }

        items = menu.getItems();
        assertEquals(2, items.size());
    }

    @Test
    public void removeItemPreExisting() {
        menu.addItem(bluePlate);
        menu.addItem(redPlate);
        menu.addItem(greenPlate);
        ArrayList<Plate> items = menu.getItems();
        assertEquals(3, items.size());
        assertEquals(bluePlate, items.get(0));
        assertEquals(redPlate, items.get(1));
        assertEquals(greenPlate, items.get(2));

        try {
            menu.removeItem(redPlate);
        } catch (PlateNotInListException e) {
            fail("threw exception");
        }

        items = menu.getItems();
        assertEquals(2, items.size());
        assertEquals(bluePlate, items.get(0));
        assertEquals(greenPlate, items.get(1));
    }

    @Test
    public void removeItemFromNothing() {
        ArrayList<Plate> items = menu.getItems();
        assertEquals(0, items.size());

        try {
            menu.removeItem(greenPlate);
            fail("didn't throw exception");
        } catch (PlateNotInListException e) {
            // success
        }

        items = menu.getItems();
        assertEquals(0, items.size());
    }

    @Test
    public void removeSeveralItems() {
        menu.addItem(redPlate);
        menu.addItem(bluePlate);
        ArrayList<Plate> items = menu.getItems();
        assertEquals(2, items.size());

        try {
            menu.removeItem(redPlate);
        } catch (PlateNotInListException e) {
            fail("exception thrown");
        }

        items = menu.getItems();
        assertEquals(1, items.size());
        assertEquals(bluePlate, items.get(0));

        try {
            menu.removeItem(bluePlate);
        } catch (PlateNotInListException e) {
            fail("exception thrown");
        }

        items = menu.getItems();
        assertEquals(0, items.size());
    }

    @Test
    public void toJsonTest() {
        JSONObject json = menu.toJson();

        assertEquals(0, json.getJSONArray("items").length());

        menu.addItem(redPlate);
        menu.addItem(bluePlate);
        menu.addItem(greenPlate);
        json = menu.toJson();
        JSONArray jsonItems = json.getJSONArray("items");

        assertEquals(3, jsonItems.length());
    }
}

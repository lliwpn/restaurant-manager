package model;

import model.exceptions.MoneyFormatException;
import model.exceptions.ProfitErrException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PlateTest {

    Plate plate1;
    Plate plate2;
    Plate plate3;
    Plate plateErrorMoney;
    Plate plateErrorProfit;

    @BeforeEach
    public void setup() {
        try {
            plate1 = new Plate("green", 0, 0);
            plate2 = new Plate("red", 1.50, 0.50);
            plate3 = new Plate("blue", 1.00, 1.00);
        } catch (MoneyFormatException e) {
            fail("money format error");
        } catch (ProfitErrException e) {
            fail("profit (operating > profit) error");
        }
    }

    @Test
    public void plateImproperFormatTest() {
        try {
            plateErrorMoney = new Plate("uh oh", -1, -10);
            fail("construction succeeded when it was intended to fail");
        } catch (MoneyFormatException e) {
            // success
        } catch (ProfitErrException e) {
            fail("error was thrown that was not supposed to be thrown");
        }

        try {
            plateErrorMoney = new Plate("uh oh", 1, -1);
            fail("construction succeeded when it was intended to fail");
        } catch (MoneyFormatException e) {
            // success
        } catch (ProfitErrException e) {
            fail("error was thrown that was not supposed to be thrown");
        }
    }

    @Test
    public void plateImproperProfitTest() {
        try {
            plateErrorProfit = new Plate("uh oh", 3, 4);
            fail("construction succeeded when it was intended to fail");
        } catch (MoneyFormatException e) {
            fail("error was thrown that was not supposed to be thrown");
        } catch (ProfitErrException e) {
            // success
        }
    }

    @Test
    public void plateImproperBothTest() {
        try {
            plateErrorProfit = new Plate("uh oh ", -2, -1);
            fail("construction succeeded when it was intended to fail");
        } catch (Exception e) {
            // success
        }
    }

    @Test
    public void plateProperTest() {
        assertEquals("green", plate1.getId());
        assertEquals(0, plate1.getPrice());
        assertEquals(0, plate1.getOperatingPrice());

        assertEquals("red", plate2.getId());
        assertEquals(1.50, plate2.getPrice());
        assertEquals(0.50, plate2.getOperatingPrice());

        plate1.setOperatingPrice(1.00);
        plate2.setPrice(2.50);

        assertEquals(1.00, plate1.getOperatingPrice());
        assertEquals(2.50, plate2.getPrice());
    }

    @Test
    public void getItemProfitTest() {
        assertEquals(0, plate1.getItemProfit());
        assertEquals(1.00, plate2.getItemProfit());
        assertEquals(0, plate3.getItemProfit());
    }

    @Test
    public void toJsonTest() {
        JSONObject plate2Json = plate2.toJson();
        assertEquals("red", plate2Json.getString("id"));
        assertEquals(1.50, plate2Json.getDouble("price"));
        assertEquals(0.50, plate2Json.getDouble("operatingPrice"));

        JSONObject plate3Json = plate3.toJson();
        assertEquals("blue", plate3Json.getString("id"));
        assertEquals(1.00, plate3Json.getDouble("price"));
        assertEquals(1.00, plate3Json.getDouble("operatingPrice"));
    }
}

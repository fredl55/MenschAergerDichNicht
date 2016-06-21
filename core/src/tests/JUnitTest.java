import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Interface.Message;
import com.gruppe4.menschaergerdichnicht.Logic.Pin;
import com.gruppe4.menschaergerdichnicht.MyAssets;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Markus on 15.06.2016.
 */
public class JUnitTest{

    MyAssets assets1 = new MyAssets();

    //testen, ob die übergebene Message des Spieler 1:1 gespeichert wird
    @Test
    public void messageTest(){
        Message m = new Message("NewPlayer", "Hallo, ich bin ein neuer Spieler");

        assertEquals(m.getMessage(),"Hallo, ich bin ein neuer Spieler");
    }

    //testen, ob der offset der y-Position des Pins richtig berechnet wird, fieldOffsetY = 85
    //gdxruntimeexception: Asset not loaded: bluePin.png
    //@Test
    public void pinPosTest(){
        String color = "blue";
        MyAssets.loadAllAssets();
        Pin pin = new Pin(1, color);
        pin.setPinPosition(10,50);

        assertEquals(135.0, pin.getPosY(), 0);
    }

    //testen, ob man die Farbe durch angabe des Namens bekommt
    @Test
    public void colorFromNameTest(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.1");

        assertEquals("blue", f.getColorFromName("Blau"));
    }

    @Test
    public void colorFromName2Test(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.2");

        assertEquals("red", f.getColorFromName("Rot"));
    }

    @Test
    public void colorFromName3Test(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.3");

        assertEquals("green", f.getColorFromName("Grün"));
    }

    @Test
    public void colorFromName4Test(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.4");

        assertEquals("yellow", f.getColorFromName("Gelb"));
    }

    //testen, ob man die durch Angabe der falschen Farbe null zurückbekommt
    @Test
    public void wrongColorNameTest(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.5");

        assertEquals(null, f.getColorFromName("Falsche Farbe"));
    }

    //testen, ob die PositionNumber des Feldes richtig vergeben wird
    @Test
    public void checkPosNumber() {
        Field f = new Field(0, 0, "Spielfeld", "Feld Nr.6");

        assertEquals(f.getPositionNr(), 6);
    }



}
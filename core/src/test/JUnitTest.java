import com.gruppe4.menschaergerdichnicht.Interface.Message;
import com.gruppe4.menschaergerdichnicht.Logic.Pin;
import com.gruppe4.menschaergerdichnicht.Fields.Field;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Markus on 15.06.2016.
 */
public class JUnitTest {

    //testen, ob die übergebene Message des Spieler 1:1 gespeichert wird
    @Test
    public void messageTest(){
        Message m = new Message("NewPlayer", "Hallo, ich bin ein neuer Spieler");

        assertEquals(m.getMessage(),"Hallo, ich bin ein neuer Spieler");
    }

    //testen, ob der offset der y-Position des Pins richtig berechnet wird, fieldOffsetY = 85
    @Test
    public void pinPosTest(){
        Pin pin = new Pin(1, "Pin");
        pin.setPinPosition(10,50);

        assertEquals(135, pin.getPosY());
    }

    //testen, ob man die Farbe durch angabe des Namens bekommt
    @Test
    public void colorFromNameTest(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.1");

        assertEquals("blue", f.getColorFromName("Blau"));
    }

    //testen, ob man die durch Angabe der falschen Farbe null zurückbekommt
    @Test
    public void wrongColorNameTest(){
        Field f = new Field(0,0,"Spielfeld","Feld Nr.1");

        assertEquals(null, f.getColorFromName("Falsche Farbe"));
    }

    //weitere Tests folgen noch

}
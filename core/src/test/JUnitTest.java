package test;

import com.gruppe4.menschaergerdichnicht.Interface.Message;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by Markus on 15.06.2016.
 */
public class JUnitTest {
    @Test
    public void messageTest(){
        Message m = new Message("NewPlayer", null);
        assertFalse(m.getInfo().equals(""));
    }
}
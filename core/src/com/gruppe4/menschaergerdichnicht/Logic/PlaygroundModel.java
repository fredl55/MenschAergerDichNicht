package com.gruppe4.menschaergerdichnicht.Logic;

import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Fields.FieldType;
import com.gruppe4.menschaergerdichnicht.Fields.GoalField;
import com.gruppe4.menschaergerdichnicht.Fields.HomeField;
import com.gruppe4.menschaergerdichnicht.Fields.StartField;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Created by manfrededer on 03.05.16.
 */
public class PlaygroundModel {
    public static ArrayList<HomeField> homeFields = new ArrayList<HomeField>();
    public static ArrayList<StartField> startFields = new ArrayList<StartField>();
    public static ArrayList<GoalField> goalFields = new ArrayList<GoalField>();
    public static ArrayList<Field> normalFields = new ArrayList<Field>();
    public static ArrayList<Pin> pins = new ArrayList<Pin>();

    public static void createPinsForColor(String c){
        ArrayList<HomeField> homeFieldsForColor = getHomeFieldsForColor(c);
        for(int i=0;i<4;i++){
            HomeField help = homeFieldsForColor.get(i);
            Pin p = new Pin(c);
            p.setPinPosition(help.getX(),help.getY());
            p.setNumber(help.getPositionNr());

            pins.add(p);
        }
    }

    private static ArrayList<HomeField> getHomeFieldsForColor(String c) {
        ArrayList<HomeField> help = new ArrayList<HomeField>();
        int i =0;
        while(i < homeFields.size() && help.size() != 4){
            if(homeFields.get(i).getColor() == c){
                help.add(homeFields.get(i));
            }
            i++;
        }
        return help;
    }


}

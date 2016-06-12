package com.gruppe4.menschaergerdichnicht.Logic;

import com.badlogic.gdx.Gdx;
import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Fields.FieldType;
import com.gruppe4.menschaergerdichnicht.Fields.GoalField;
import com.gruppe4.menschaergerdichnicht.Fields.HomeField;
import com.gruppe4.menschaergerdichnicht.Fields.StartField;

import java.awt.Color;
import java.lang.reflect.Array;
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
        for(int i=0;i<homeFieldsForColor.size(); i++){
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
            String co = homeFields.get(i).getColor();
            if(co.compareTo(c) == 0){
                help.add(homeFields.get(i));
            }
            i++;
        }
        return help;
    }

    public static void selectPin(Pin current){
        ArrayList<Pin> myPins = getPinsForColor(current.getPinColor());
        for(int i=0;i< myPins.size();i++){
            if(current.equals(myPins.get(i))){
                myPins.get(i).switchSelection();
            } else if(myPins.get(i).isSelected()){
                myPins.get(i).switchSelectionOff();
            }

        }
    }


    public static boolean AreAllPinsForColorInHome(String myColor) {
        ArrayList<Pin> myPins = getPinsForColor(myColor);
        boolean retVal = true;
        int i=0;
        while(retVal ==true && i<myPins.size()){
            if(myPins.get(i).getCurrentType()!=FieldType.HomeField){
                retVal = false;
            }
            i++;
        }
        return retVal;
    }

    private static ArrayList<Pin> getPinsForColor(String myColor){
        ArrayList<Pin> myPins = new ArrayList<Pin>();
        for(int i =0; i < pins.size();i++){
            if(pins.get(i).getPinColor().compareTo(myColor)==0){
                myPins.add(pins.get(i));
            }
        }
        return myPins;
    }

    public static Pin getSelectedPin(String myColor){
        ArrayList<Pin> myPins = getPinsForColor(myColor);
        Pin ret = null;
        int i =0;
        while(ret == null && i<myPins.size()){
            if(myPins.get(i).isSelected()){
                ret = myPins.get(i);
            }
            i++;
        }
        return ret;
    }

    public static void move(String myColor, int number){
        Pin selectedPin = getSelectedPin(myColor);
        selectedPin.switchSelectionOff();
        if(selectedPin.getCurrentType() == FieldType.HomeField && number == 6){
            selectedPin.setCurrentType(FieldType.StartField);
            StartField start = getStartFieldForColor(myColor);
            selectedPin.setPositionNr(start.getPositionNr());
            movePin(new Draw(selectedPin.getNumber(),selectedPin.getPinColor(),selectedPin.getCurrentType(),0,0));
        } else if(selectedPin.getCurrentType() == FieldType.StartField || selectedPin.getCurrentType() == FieldType.NormalField){
            selectedPin.setOldPositionNr(selectedPin.getPositionNr());
            if(selectedPin.getOldPositionNr()+number<=40){
                selectedPin.setPositionNr(selectedPin.getOldPositionNr()+number);
            } else {
                selectedPin.setPositionNr(selectedPin.getOldPositionNr()+number-40);
                //weiter
            }
            selectedPin.setCurrentType(FieldType.NormalField);
            movePin(new Draw(selectedPin.getNumber(),selectedPin.getPinColor(),selectedPin.getCurrentType(),selectedPin.getOldPositionNr(),selectedPin.getPositionNr()));

        }
    }

    private static StartField getStartFieldForColor(String myColor){
        StartField ret = null;
        for(int i =0; i < startFields.size() && ret==null;i++){
            if(startFields.get(i).getColor().compareTo(myColor)==0){
                ret = startFields.get(i);
            }
        }
        return ret;
    }

    private static Pin getPinFromColor(String myColor,int id){
        ArrayList<Pin> myPins = getPinsForColor(myColor);
        Pin help = null;
        for(int i =0; i < myPins.size() && help == null; i++){
            if(myPins.get(i).getNumber()==id){
                help = myPins.get(i);
            }
        }
        return help;

    }

    public static void movePin(Draw draw) {
        Pin pinToMove = getPinFromColor(draw.getColor(), draw.getPinId());
        if(draw.getFieldType().compareTo(FieldType.StartField)==0){
            StartField start = getStartFieldForColor(draw.getColor());
            pinToMove.setPinPosition(start.getX(),start.getY());
            pinToMove.setPositionNr(start.getPositionNr());
        } else if(draw.getFieldType().compareTo(FieldType.NormalField)==0){
            Field normal = getNormalFieldFromPos(draw.getTo());
            pinToMove.setPinPosition(normal.getX(),normal.getY());
            pinToMove.setPositionNr(normal.getPositionNr());
        }
    }

    private static Field getNormalFieldFromPos(int to) {
        Field ret = null;
        for(int i=0; i < normalFields.size() && ret == null; i++){
            if(normalFields.get(i).getPositionNr() == to){
                ret = normalFields.get(i);
            }
        }
        return ret;
    }

    public static Pin tryGetTappedPin(float x, float y,String myColor,int rollValue) {
        ArrayList<Pin> myPins = getPinsForColor(myColor);
        Pin ret = null;
        for (int i=0; i < myPins.size() && ret ==null;i++){
            if(myPins.get(i).getMyPin().getBoundingRectangle().contains(x,y)){
                PlaygroundModel.selectPin(myPins.get(i));
                PlaygroundModel.move(myColor, rollValue);
                ret = myPins.get(i);
            }
        }
        return ret;
    }
}

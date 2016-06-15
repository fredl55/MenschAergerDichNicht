package com.gruppe4.menschaergerdichnicht.Logic;

import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Fields.FieldType;
import com.gruppe4.menschaergerdichnicht.Fields.GoalField;
import com.gruppe4.menschaergerdichnicht.Fields.HomeField;
import com.gruppe4.menschaergerdichnicht.Fields.StartField;

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


    public static void createPinsForColor(String color){
        ArrayList<HomeField> homeFieldsForColor = getHomeFieldsForColor(color);
        for(int i=0;i<homeFieldsForColor.size(); i++){
            HomeField homefield = homeFieldsForColor.get(i);
            Pin p = new Pin(homefield.getPositionNr(),color);
            p.setField(homefield);
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
        //from the homefield to the startfield
        if(selectedPin.getCurrentType() == FieldType.HomeField && number == 6){
            StartField start = getStartFieldForColor(myColor);
            selectedPin.setField(start);
            checkIfThereAlreadyIsAPinOnThisPosition(selectedPin.getPositionNr(),selectedPin);
        }
        //from a normalFieldTo another NormalField or the GoalField
        else if(selectedPin.getCurrentType() == FieldType.StartField || selectedPin.getCurrentType() == FieldType.NormalField){
            int endFieldNr = getStartFieldForColor(selectedPin.getPinColor()).getPositionNr()-1;
            int nextPositionNr = selectedPin.getPositionNr()+number;
            selectedPin.setOldPositionNr(selectedPin.getPositionNr());
            //Check for not moving to far
            if(nextPositionNr>40){
                nextPositionNr = nextPositionNr - 40;
            }
            //Calculate the right GoalField
            if(nextPositionNr>endFieldNr && selectedPin.getOldPositionNr()<=endFieldNr){
                int goalFieldSteps = nextPositionNr - endFieldNr;
                if(goalFieldSteps<=4 && goalFieldIsFree(goalFieldSteps,selectedPin.getPinColor())){
                    GoalField goalField = getGoalFieldFromColor(selectedPin.getPinColor(),goalFieldSteps);
                    selectedPin.setField(goalField);
                }
            }
            //We draw Normal
            else {
                Field normal = getNormalFieldFromPos(nextPositionNr);
                selectedPin.setField(normal);
                checkIfThereAlreadyIsAPinOnThisPosition(selectedPin.getPositionNr(),selectedPin);
            }
        } else if(selectedPin.getCurrentType().compareTo(FieldType.GoalField)==0){
            if(selectedPin.getPositionNr()+number<=4 && goalFieldIsFree(selectedPin.getPositionNr()+number,selectedPin.getPinColor())){
                GoalField goalField = getGoalFieldFromColor(selectedPin.getPinColor(),selectedPin.getPositionNr()+number);
                selectedPin.setField(goalField);
            }
        }
    }

    private static boolean goalFieldIsFree(int goalFieldSteps,String color) {
        ArrayList<Pin> myPins = getPinsForColor(color);
        boolean retVal = true;
        for(int i=0;i<myPins.size() && retVal;i++){
            if(myPins.get(i).getPositionNr()==goalFieldSteps && myPins.get(i).getCurrentType().compareTo(FieldType.GoalField)==0){
                retVal = false;
            }
        }
        return retVal;
    }

    private static StartField getStartFieldForColor(String myColor){
        StartField ret = null;
        for(int i =0; i < startFields.size() && ret==null; i++){
            if (startFields.get(i).getColor().compareTo(myColor)==0){
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
            pinToMove.setField(start);
            checkIfThereAlreadyIsAPinOnThisPosition(start.getPositionNr(), pinToMove);
        } else if(draw.getFieldType().compareTo(FieldType.NormalField)==0){
            Field normal = getNormalFieldFromPos(draw.getTo());
            pinToMove.setField(normal);
            checkIfThereAlreadyIsAPinOnThisPosition(normal.getPositionNr(),pinToMove);
        } else if(draw.getFieldType().compareTo(FieldType.GoalField)==0){
            GoalField goalField = getGoalFieldFromColor(draw.getColor(),draw.getTo());
            pinToMove.setField(goalField);
        }
    }

    public static boolean checkForWin(String color) {
        boolean won = true;
        ArrayList<Pin> myPins = getPinsForColor(color);
        for(int i=0; i < myPins.size() && won; i++){
            if(myPins.get(i).getCurrentType().compareTo(FieldType.GoalField)!= 0){
                won = false;
            }
        }
        return won;
    }

    private static void checkIfThereAlreadyIsAPinOnThisPosition(int positionNr,Pin notThisPin) {
        boolean found = false;
        for(int i=0; i < pins.size() && !found;i++){
            if(pins.get(i).getPositionNr()==positionNr && !pins.get(i).equals(notThisPin) &&
                    (pins.get(i).getCurrentType().compareTo(FieldType.NormalField)==0 || pins.get(i).getCurrentType().compareTo(FieldType.StartField)==0)){
                found = true;
                moveBackToHome(pins.get(i));
            }
        }
    }

    private static void moveBackToHome(Pin pin) {
        HomeField freeHomeField = getFreeHomeFieldForPin(pin.getPinColor(), pin.getNumber());
        pin.setField(freeHomeField);
    }

    private static HomeField getFreeHomeFieldForPin(String pinColor, int number) {
        ArrayList<HomeField> homeFields = getHomeFieldsForColor(pinColor);
        HomeField ret = null;
        for(int i=0; i < homeFields.size() && ret ==null;i++){
            if(homeFields.get(i).getPositionNr()==number){
                ret = homeFields.get(i);
            }
        }
        return ret;
    }

    private static GoalField getGoalFieldFromColor(String color, int to) {
        GoalField ret = null;
        for (int i=0; i< goalFields.size() && ret == null;i++){
            if(goalFields.get(i).getColor().compareTo(color)==0 && goalFields.get(i).getPositionNr() == to){
                ret = goalFields.get(i);
            }
        }
        return ret;
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

    public static boolean IsThereAPinToMove(String myColor, int number) {
        boolean retVal = false;
        //if the number is 6 and there is a pin in home
        if(number == 6 && isAPinInHome(myColor)){
            retVal = true;
        }
        else {
            ArrayList<Pin> myPins = getPinsForColor(myColor);
            for(int i=0; i < myPins.size() && !retVal;i++){
                Pin currentPin = myPins.get(i);
                if(currentPin.getCurrentType().compareTo(FieldType.StartField)==0){
                    retVal=true;
                } else if(currentPin.getCurrentType().compareTo(FieldType.NormalField)==0){
                    int endFieldNr = getStartFieldForColor(myColor).getPositionNr()-1;
                    //if there is a pin on a normal field and we can move without going over the endfield
                    if(currentPin.getPositionNr()+number<=endFieldNr || currentPin.getPositionNr()>endFieldNr){
                        retVal = true;
                    } else{
                        int stepsToEndField = endFieldNr - currentPin.getPositionNr();
                        if((number-stepsToEndField)<=4  && goalFieldIsFree(number-stepsToEndField,myColor)){
                            retVal = true;
                        }
                    }
                } else if(currentPin.getCurrentType().compareTo(FieldType.GoalField)==0){
                    if(currentPin.getPositionNr()+number<=4 && goalFieldIsFree(currentPin.getPositionNr()+number,myColor)){
                        retVal = true;
                    }
                }
            }
        }
        return retVal;
    }

    private static boolean isAPinInHome(String myColor) {
        ArrayList<Pin> myPins = getPinsForColor(myColor);
        boolean retVal = false;
        for(int i=0; i < myPins.size() && !retVal;i++){
            if(myPins.get(i).getCurrentType().compareTo(FieldType.HomeField)==0){
                retVal = true;
            }
        }
        return retVal;
    }
}

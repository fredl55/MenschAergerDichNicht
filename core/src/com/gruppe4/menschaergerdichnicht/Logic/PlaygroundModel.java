package com.gruppe4.menschaergerdichnicht.Logic;

import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Fields.FieldType;
import com.gruppe4.menschaergerdichnicht.Fields.GoalField;
import com.gruppe4.menschaergerdichnicht.Fields.HomeField;
import com.gruppe4.menschaergerdichnicht.Fields.StartField;

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
}

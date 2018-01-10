package com.keita.vccs.util;

import com.keita.vccs.blueprint.Class;
import javafx.collections.ObservableList;

public class Validation {

    public static boolean classExist(ObservableList<Class> classes, String classID) {
        for (Class cal : classes) {
            if (cal.getClassID().equals(classID)) {
                return true;
            }
        }
        return false;
    }
}

package com.keita.vccs.workstation;

import com.keita.vccs.blueprint.Class;
import javafx.collections.ObservableList;

public class Validation {

    public boolean isClassExist(ObservableList<Class> classes, String classID) {
        return classExist(classes, classID);
    }

    private boolean classExist(ObservableList<Class> classes, String classID) {
        for (Class cal : classes) {
            if (cal.getClassID().equals(classID)) {
                return true;
            }
        }
        return false;
    }


}

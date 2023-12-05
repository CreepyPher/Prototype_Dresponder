package com.example.prototype_dresponder.Constants;

public class Suggestions {

    public static final String Alert_LOWKEY = "Condition_Mild";
    public static final String Alert_MODERATEKEY = "Condition_Low";
    public static final String Alert_HIGHKEY = "Condition_Low";

    public static String FireAlertSuggestion(String condition){

        if(condition.equalsIgnoreCase(Alert_LOWKEY)) {
            return "Suggested item/kit (Mild): " + "fire extinguisher or any thing to subdue the Fire,\n"
                    + "cold water, Wet compress, Pain reliever, Bandage or band-aid";
        } else if (condition.equalsIgnoreCase(Alert_MODERATEKEY)) {
            return "Suggested item/kit (Moderate): " + ",\n"
                    + "";
        }

        return "N?A";
    }

}

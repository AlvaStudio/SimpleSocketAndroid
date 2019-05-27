package com.alvastudio.simplesocket.Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utils {
    public static boolean isJSONValid(String test) {
        try {
            new JSONParser().parse(test);
        } catch (org.json.simple.parser.ParseException e) {
            return false;
        }

        return true;
    }

    public static JSONObject getJSONInString(String string){

        JSONParser parser = new JSONParser();
        JSONObject js;
        try {
            js = (JSONObject) parser.parse(string);
            return js;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

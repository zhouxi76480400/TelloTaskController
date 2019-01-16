package org.enes.tellotaskcontroller.utils;

import java.util.HashMap;

public class Utils {

    public static HashMap<String, String> statusDataToHashMap(String status_data) {
        HashMap<String,String> hashMap = null;
        if(status_data != null) {
            hashMap = new HashMap<>();
            String [] keyAndValues = status_data.split(";");
            for (int i = 0 ; i < keyAndValues.length ; i ++) {
                String keyAndValue = keyAndValues[i];
                if(!keyAndValue.equals("\r\n")) {
                    hashMap.put(String.valueOf(i),keyAndValue);
                }
            }
        }
        return hashMap;
    }
}

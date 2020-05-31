package com.company.utils;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public class CallUtils {
    public static boolean checkNumber(String number){
        return !Strings.isNullOrEmpty(number);
    }

    public static String parseError(String callSid, String from, String to){
        List<String> errors = new ArrayList<>();
        if(!checkNumber(from)){
            errors.add("invalid 'from' number");
        }
        if(!checkNumber(to)){
            errors.add("invalid 'to' number");
        }

        return String.join(", ", errors);
    }
}

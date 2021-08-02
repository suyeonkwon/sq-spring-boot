package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    private static boolean matcherFind(String target, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

        return matcherFind(target, regex);
    }
    public static boolean isRegexPassword(String target) {
        String regex = "\\w{6,60}";
        return matcherFind(target, regex);
    }

    public static boolean isRegexPhone(String target){
        String regex = "\\d{3}-\\d{4}-\\d{4}";
        return matcherFind(target, regex);
    }

    public static boolean isRegexPin(String target){
        String regex = "[0-9]{4}";
        if(target.length() == 4){
            return matcherFind(target,regex);
        }else{
            return false;
        }
    }
}


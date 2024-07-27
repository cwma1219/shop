package com.example.rd.util;

import io.micrometer.common.util.StringUtils;

public class RegexUtil {
    public static final String PHONE_REGEX = "^09\\d{8}$";

    public static boolean isPhoneValid(String phone){
        return check(phone);
    }

    private static boolean check(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        return str.matches(RegexUtil.PHONE_REGEX);
    }
}

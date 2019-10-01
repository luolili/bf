package com.luo.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String salt = "1a2b";
    public static String md5(String password) {
        return DigestUtils.md5Hex(password);
    }
     public static String inputPassFormPass(String password) {
        String data = salt+password;
         return md5(data);
    }

    public static String formPassToDBPass(String formPass,String salt) {
        String data = salt +formPass;
        return md5(data);
    }
    public static String inputPassToDBPass(String input,String salt) {
        String formPass = inputPassFormPass(input);
        return  formPassToDBPass(formPass, salt);
    }

    public static void main(String[] args) {
        String xe = inputPassToDBPass("123", "xe");

        System.out.println(xe);//c10088331ab1a28a9c49bdc168095b0c
    }

}

package com.luo.miaosha.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CodeMsg {

    private int code;
    private String msg;

    private CodeMsg() {

    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server_error");
    //login
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "session_error");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "password_empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "mobile_empty");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "mobile_not_exist");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "password_error");

    //item
    public static CodeMsg success = new CodeMsg(0, "success");
    //order
    //public static CodeMsg success = new CodeMsg(0, "success");

}

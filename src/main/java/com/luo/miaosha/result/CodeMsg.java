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

    public CodeMsg fillArgs(Object...args) {
       int code = this.code;
       String msg = String.format(this.msg,args);
        return new CodeMsg(code, msg);
    }

    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server_error");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "request_illegal");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "%s");
    //login
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "session_error");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "password_empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "mobile_empty");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "mobile_not_exist");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "password_error");

    //item
    public static CodeMsg success = new CodeMsg(0, "success");
    //order
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(5004001, "订单不存在");
    //public static CodeMsg success = new CodeMsg(0, "success");
    //miaosha:5005XX
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500, "MIAOSHA_OVER");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500510, "repeate_miaosha");

}

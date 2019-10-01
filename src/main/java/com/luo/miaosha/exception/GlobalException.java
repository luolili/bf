package com.luo.miaosha.exception;

import com.luo.miaosha.result.CodeMsg;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException{

    private CodeMsg cm;
    public GlobalException() {
    }
     public GlobalException(CodeMsg cm) {
        super();
        this.cm = cm;
    }

}

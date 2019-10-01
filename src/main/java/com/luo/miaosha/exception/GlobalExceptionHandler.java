package com.luo.miaosha.exception;

import com.luo.miaosha.result.CodeMsg;
import com.luo.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest req,Exception ex) {
        if (ex instanceof GlobalException) {
            GlobalException globalException = (GlobalException) ex;
            return Result.error(globalException.getCm());

        }
        if (ex instanceof BindException) {//spring validation
            BindException e = (BindException) ex;
            List<ObjectError> allErrors = e.getAllErrors();
            ObjectError objectError = allErrors.get(0);

            String msg = objectError.getDefaultMessage();

            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));

        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}

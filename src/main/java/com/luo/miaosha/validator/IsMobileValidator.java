package com.luo.miaosha.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{

    private boolean required=false;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
return false;
        }else {
            if (StringUtils.isEmpty(value)) {
return true;
            }else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void initialize(IsMobile constraintAnnotation) {

         required = constraintAnnotation.required();

    }
}

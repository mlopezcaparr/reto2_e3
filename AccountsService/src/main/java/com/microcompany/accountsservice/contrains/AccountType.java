package com.microcompany.accountsservice.contrains;

import com.microcompany.accountsservice.model.Account;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountType.Validator.class)
public @interface AccountType {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<AccountType, String> {
        @Override
        public void initialize(AccountType constrainAnnotation) {}

        @Override
        public boolean isValid(String value, final ConstraintValidatorContext constraintValidatorContext) {
            if(value == null)
                return false;
            return value.equalsIgnoreCase("Personal") || value.equals("Company");
        }
    }
}

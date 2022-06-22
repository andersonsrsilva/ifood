package br.com.ifood.cadastro.rest.dto;

import javax.validation.ConstraintValidatorContext;

public interface DTO {

    default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }

}

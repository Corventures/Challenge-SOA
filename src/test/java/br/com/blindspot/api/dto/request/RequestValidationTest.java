package br.com.blindspot.api.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void loginDtoDeveExigirUsuarioESenha() {
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(new LoginDTO("", ""));

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Usuário é obrigatório", "Senha é obrigatória");
    }

    @Test
    void cadastroDtoDeveValidarTamanhoDeUsuarioESenha() {
        Set<ConstraintViolation<CadastroDTO>> violations = validator.validate(new CadastroDTO("ab", "123"));

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Usuário deve ter entre 3 e 100 caracteres")
                .contains("Senha deve ter entre 8 e 72 caracteres");
    }
}
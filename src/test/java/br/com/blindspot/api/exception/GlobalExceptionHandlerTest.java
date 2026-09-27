package br.com.blindspot.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveMapearAutenticacaoComoUnauthorized() {
        var response = handler.handleAuthentication(new BadCredentialsException("token inválido"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo(Map.of("erro", "Usuário não autenticado ou token inválido"));
    }

    @Test
    void deveMapearAcessoNegadoComoForbidden() {
        var response = handler.handleAccessDenied(new AccessDeniedException("sem permissão"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isEqualTo(Map.of("erro", "Acesso negado: você não tem permissão para acessar este recurso"));
    }

    @Test
    void deveMapearUsuarioExistenteComoConflict() {
        var response = handler.handleUserAlreadyExists(new UserAlreadyExistsException("Nome de usuário já cadastrado"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(Map.of("erro", "Nome de usuário já cadastrado"));
    }

    @Test
    void deveMapearRecursoInexistenteComoNotFound() {
        var response = handler.handleIllegalArgument(new IllegalArgumentException("Versão não encontrada com ID: 111"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("erro", "Versão não encontrada com ID: 111"));
    }
}
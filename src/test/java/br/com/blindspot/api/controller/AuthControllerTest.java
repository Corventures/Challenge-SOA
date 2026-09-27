package br.com.blindspot.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.blindspot.api.dto.request.CadastroDTO;
import br.com.blindspot.api.dto.request.LoginDTO;
import br.com.blindspot.api.dto.response.MensagemResponseDTO;
import br.com.blindspot.api.dto.response.TokenResponseDTO;
import br.com.blindspot.api.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginDeveRetornarOkComToken() {
        when(authService.login(new LoginDTO("usuario", "senha123")))
                .thenReturn(new TokenResponseDTO("token-jwt"));

        ResponseEntity<TokenResponseDTO> response = authController.login(new LoginDTO("usuario", "senha123"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isEqualTo("token-jwt");
    }

    @Test
    void cadastroDeveRetornarCreatedComMensagem() {
        when(authService.cadastro(new CadastroDTO("usuario", "senha12345")))
                .thenReturn(new MensagemResponseDTO("Usuário cadastrado com sucesso"));

        ResponseEntity<MensagemResponseDTO> response = authController.cadastro(
                new CadastroDTO("usuario", "senha12345"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().mensagem()).isEqualTo("Usuário cadastrado com sucesso");
    }
}
package br.com.blindspot.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.blindspot.api.domain.AppUser;
import br.com.blindspot.api.dto.request.CadastroDTO;
import br.com.blindspot.api.dto.request.LoginDTO;
import br.com.blindspot.api.dto.response.MensagemResponseDTO;
import br.com.blindspot.api.dto.response.TokenResponseDTO;
import br.com.blindspot.api.exception.UserAlreadyExistsException;
import br.com.blindspot.api.repository.AppUserRepository;
import br.com.blindspot.api.security.TokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginDeveRetornarTokenGerado() {
        LoginDTO data = new LoginDTO("usuario", "senha123");
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn("usuario");
        when(tokenService.generateToken("usuario")).thenReturn("token-jwt");

        TokenResponseDTO response = authService.login(data);

        assertThat(response.token()).isEqualTo("token-jwt");
    }

    @Test
    void cadastroDeveSalvarNovoUsuario() {
        CadastroDTO data = new CadastroDTO("usuario", "senha12345");

        when(appUserRepository.findByUsername("usuario")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha12345")).thenReturn("senha-hash");

        MensagemResponseDTO response = authService.cadastro(data);

        verify(appUserRepository).save(argThat(user ->
                user.getUsername().equals("usuario")
                        && user.getSenhaHash().equals("senha-hash")
                        && user.getRoleName().equals("USER")));
        assertThat(response.mensagem()).isEqualTo("Usuário cadastrado com sucesso");
    }

    @Test
    void cadastroDeveFalharQuandoUsuarioJaExistir() {
        CadastroDTO data = new CadastroDTO("usuario", "senha12345");

        when(appUserRepository.findByUsername("usuario"))
                .thenReturn(Optional.of(new AppUser("usuario", "hash", "USER")));

        assertThatThrownBy(() -> authService.cadastro(data))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Nome de usuário já cadastrado");
    }
}
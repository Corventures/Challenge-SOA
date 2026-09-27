package br.com.blindspot.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.blindspot.api.dto.request.CadastroDTO;
import br.com.blindspot.api.dto.request.LoginDTO;
import br.com.blindspot.api.dto.response.MensagemResponseDTO;
import br.com.blindspot.api.dto.response.UserInfoResponseDTO;
import br.com.blindspot.api.dto.response.TokenResponseDTO;
import br.com.blindspot.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Autenticação")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Realiza o login do usuário", description = "Autentica o usuário com base no username e senha fornecidos e retorna um token JWT válido para acesso aos recursos protegidos da API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token JWT retornado"),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos (ex: campos vazios)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Autenticação falhou: Usuário ou senha incorretos", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO data) {
        var response = authService.login(data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Realiza o cadastro de um novo usuário", description = "Cria um novo usuário no banco de dados e retorna uma mensagem de confirmação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: campos vazios ou senha curta)", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito: O username informado já existe no sistema", content = @Content)
    })
    @PostMapping("/cadastro")
    public ResponseEntity<MensagemResponseDTO> cadastro(@RequestBody @Valid CadastroDTO data) {
        var response = authService.cadastro(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Retorna o usuário autenticado", description = "Exibe o nome de usuário e as permissões do token atual.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDTO> me(Authentication authentication) {
        var authorities = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return ResponseEntity.ok(new UserInfoResponseDTO(authentication.getName(), authorities));
    }

    @Operation(summary = "Área restrita para administradores", description = "Endpoint de exemplo para diferenciar acesso de ADMIN e USER.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<MensagemResponseDTO> admin() {
        return ResponseEntity.ok(new MensagemResponseDTO("Acesso liberado para administrador"));
    }
}

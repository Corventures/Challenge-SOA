package br.com.blindspot.api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.blindspot.api.dto.response.VersaoResumoDTO;
import br.com.blindspot.api.service.VersaoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Modelos")
@RequestMapping("/api/modelos/{modeloId}")
public class ModeloController {

    private final VersaoService versaoService;

    public ModeloController(VersaoService versaoService) {
        this.versaoService = versaoService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/anos")
    public ResponseEntity<List<Integer>> obterAnosModelo(@PathVariable Long modeloId) {
        List<Integer> anos = versaoService.obterAnosModelo(modeloId);
        return ResponseEntity.ok(anos);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/versoes")
    public ResponseEntity<List<VersaoResumoDTO>> obterVersoes(
            @PathVariable Long modeloId,
            @RequestParam(required = false) Integer ano) {
        List<VersaoResumoDTO> versoes = versaoService.buscarVersoes(null, modeloId, ano, null);
        return ResponseEntity.ok(versoes);
    }
}

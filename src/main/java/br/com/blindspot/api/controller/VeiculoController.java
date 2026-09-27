package br.com.blindspot.api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.blindspot.api.dto.response.VersaoDetalhesDTO;
import br.com.blindspot.api.dto.response.VersaoResumoDTO;
import br.com.blindspot.api.service.VersaoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Veículos")
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VersaoService versaoService;

    public VeiculoController(VersaoService versaoService) {
        this.versaoService = versaoService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/busca")
    public ResponseEntity<List<VersaoResumoDTO>> buscar(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long modeloId,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Long versaoId) {
        List<VersaoResumoDTO> resultados = versaoService.buscarVersoes(marcaId, modeloId, ano, versaoId);
        return ResponseEntity.ok(resultados);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{idVersao}")
    public ResponseEntity<VersaoDetalhesDTO> obterDetalhes(@PathVariable Long idVersao) {
        VersaoDetalhesDTO detalhes = versaoService.obterDetalhesVersao(idVersao);
        return ResponseEntity.ok(detalhes);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{idVersao}/mais-comparados")
    public ResponseEntity<List<VersaoResumoDTO>> obterMaisComparados(@PathVariable Long idVersao) {
        List<VersaoResumoDTO> maisComparados = versaoService.obterMaisComparados(idVersao);
        return ResponseEntity.ok(maisComparados);
    }
}

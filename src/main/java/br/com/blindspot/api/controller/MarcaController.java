package br.com.blindspot.api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.blindspot.api.dto.response.MarcaResponseDTO;
import br.com.blindspot.api.dto.response.ModeloResponseDTO;
import br.com.blindspot.api.service.MarcaService;
import br.com.blindspot.api.service.ModeloService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Marcas")
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    private final ModeloService modeloService;

    public MarcaController(MarcaService marcaService, ModeloService modeloService) {
        this.marcaService = marcaService;
        this.modeloService = modeloService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> obterTodasMarcas(
            @RequestParam(required = false) Boolean populares) {
        List<MarcaResponseDTO> marcas;

        if (populares != null && populares) {
            marcas = marcaService.obterMarcasPopulares();
        } else {
            marcas = marcaService.obterTodasMarcas();
        }

        return ResponseEntity.ok(marcas);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{marcaId}/modelos")
    public ResponseEntity<List<ModeloResponseDTO>> obterModelosPorMarca(@PathVariable Long marcaId) {
        List<ModeloResponseDTO> modelos = modeloService.obterModelosPorMarca(marcaId);
        return ResponseEntity.ok(modelos);
    }
}

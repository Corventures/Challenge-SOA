package br.com.blindspot.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.blindspot.api.domain.Modelo;
import br.com.blindspot.api.dto.response.ModeloResponseDTO;
import br.com.blindspot.api.repository.ModeloRepository;

@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;

    ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public List<ModeloResponseDTO> obterModelosPorMarca(Long marcaId) {
        return modeloRepository.findByMarcaId(marcaId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ModeloResponseDTO obterModeloPorId(Long id) {
        return modeloRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Modelo não encontrado com ID: " + id));
    }

    private ModeloResponseDTO convertToDTO(Modelo modelo) {
        return new ModeloResponseDTO(
                modelo.getId(),
                modelo.getNome(),
                modelo.getMarca().getId(),
                modelo.getMarca().getNome());
    }
}

package br.com.blindspot.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.blindspot.api.domain.Marca;
import br.com.blindspot.api.dto.response.MarcaResponseDTO;
import br.com.blindspot.api.repository.MarcaRepository;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public List<MarcaResponseDTO> obterTodasMarcas() {
        return marcaRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<MarcaResponseDTO> obterMarcasPopulares() {
        return obterTodasMarcas();
    }

    public MarcaResponseDTO obterMarcaPorId(Long id) {
        return marcaRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada com ID: " + id));
    }

    private MarcaResponseDTO convertToDTO(Marca marca) {
        return new MarcaResponseDTO(
            marca.getId(),
            marca.getNome(),
            marca.getUrlLogo()
        );
    }
}

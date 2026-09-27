package br.com.blindspot.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.blindspot.api.domain.Modelo;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    List<Modelo> findByMarcaId(Long marcaId);
}

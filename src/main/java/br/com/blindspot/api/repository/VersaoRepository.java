package br.com.blindspot.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.blindspot.api.domain.Versao;

public interface VersaoRepository extends JpaRepository<Versao, Long> {
    List<Versao> findByModeloId(Long modeloId);

    List<Versao> findByModeloIdAndAnoModelo(Long modeloId, Integer anoModelo);

    List<Versao> findByModeloMarcaId(Long marcaId);

    List<Versao> findByModeloMarcaIdAndModeloId(Long marcaId, Long modeloId);

    List<Versao> findByAnoModelo(Integer anoModelo);

    List<Versao> findByModeloMarcaIdAndAnoModelo(Long marcaId, Integer anoModelo);

    @Query("SELECT DISTINCT v.anoFabricacao FROM Versao v WHERE v.modelo.id = :modeloId ORDER BY v.anoFabricacao DESC")
    List<Integer> findDistinctAnosFabricacaoByModeloId(@Param("modeloId") Long modeloId);

    @Query("SELECT DISTINCT v.anoModelo FROM Versao v WHERE v.modelo.id = :modeloId ORDER BY v.anoModelo DESC")
    List<Integer> findDistinctAnosModeloByModeloId(@Param("modeloId") Long modeloId);

    @Query("SELECT v FROM Versao v WHERE v.modelo.marca.id = :marcaId AND v.modelo.id = :modeloId AND v.anoModelo = :anoModelo")
    List<Versao> buscarVersoes(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId,
            @Param("anoModelo") Integer anoModelo);
}

package br.com.blindspot.api.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.blindspot.api.domain.EspecDesempenho;
import br.com.blindspot.api.domain.EspecDimensao;
import br.com.blindspot.api.domain.EspecMotor;
import br.com.blindspot.api.domain.EspecTransmissao;
import br.com.blindspot.api.domain.FotoVeiculo;
import br.com.blindspot.api.domain.Versao;
import br.com.blindspot.api.dto.response.EspecDesempenhoDTO;
import br.com.blindspot.api.dto.response.EspecDimensaoDTO;
import br.com.blindspot.api.dto.response.EspecMotorDTO;
import br.com.blindspot.api.dto.response.EspecTransmissaoDTO;
import br.com.blindspot.api.dto.response.EquipamentoDTO;
import br.com.blindspot.api.dto.response.EquipamentosCategoriadosDTO;
import br.com.blindspot.api.dto.response.FotoVeiculoDTO;
import br.com.blindspot.api.dto.response.VersaoDetalhesDTO;
import br.com.blindspot.api.dto.response.VersaoResumoDTO;
import br.com.blindspot.api.repository.EspecDesempenhoRepository;
import br.com.blindspot.api.repository.EspecDimensaoRepository;
import br.com.blindspot.api.repository.EspecMotorRepository;
import br.com.blindspot.api.repository.EspecTransmissaoRepository;
import br.com.blindspot.api.repository.FotoVeiculoRepository;
import br.com.blindspot.api.repository.VersaoRepository;

@Service
public class VersaoService {

    private final VersaoRepository versaoRepository;

    private final FotoVeiculoRepository fotoVeiculoRepository;

    private final EspecMotorRepository especMotorRepository;

    private final EspecTransmissaoRepository especTransmissaoRepository;

    private final EspecDesempenhoRepository especDesempenhoRepository;

    private final EspecDimensaoRepository especDimensaoRepository;

    VersaoService(EspecDimensaoRepository especDimensaoRepository, EspecDesempenhoRepository especDesempenhoRepository, EspecTransmissaoRepository especTransmissaoRepository, EspecMotorRepository especMotorRepository, FotoVeiculoRepository fotoVeiculoRepository, VersaoRepository versaoRepository) {
        this.especDimensaoRepository = especDimensaoRepository;
        this.especDesempenhoRepository = especDesempenhoRepository;
        this.especTransmissaoRepository = especTransmissaoRepository;
        this.especMotorRepository = especMotorRepository;
        this.fotoVeiculoRepository = fotoVeiculoRepository;
        this.versaoRepository = versaoRepository;
    }

    public List<VersaoResumoDTO> buscarVersoes(Long marcaId, Long modeloId, Integer anoModelo, Long versaoId) {
        List<Versao> versoes;

        if (versaoId != null) {
            versoes = versaoRepository.findById(versaoId)
                    .map(List::of)
                    .orElse(List.of());
        } else if (marcaId != null && modeloId != null && anoModelo != null) {
            versoes = versaoRepository.buscarVersoes(marcaId, modeloId, anoModelo);
        } else if (marcaId != null && modeloId != null) {
            versoes = versaoRepository.findByModeloMarcaIdAndModeloId(marcaId, modeloId);
        } else if (marcaId != null && anoModelo != null) {
            versoes = versaoRepository.findByModeloMarcaIdAndAnoModelo(marcaId, anoModelo);
        } else if (modeloId != null && anoModelo != null) {
            versoes = versaoRepository.findByModeloIdAndAnoModelo(modeloId, anoModelo);
        } else if (marcaId != null) {
            versoes = versaoRepository.findByModeloMarcaId(marcaId);
        } else if (modeloId != null) {
            versoes = versaoRepository.findByModeloId(modeloId);
        } else if (anoModelo != null) {
            versoes = versaoRepository.findByAnoModelo(anoModelo);
        } else {
            versoes = versaoRepository.findAll();
        }

        return versoes.stream()
                .map(this::convertToResumoDTO)
                .collect(Collectors.toList());
    }

    public VersaoDetalhesDTO obterDetalhesVersao(Long idVersao) {
        Versao versao = versaoRepository.findById(idVersao)
                .orElseThrow(() -> new IllegalArgumentException("Versão não encontrada com ID: " + idVersao));

        EspecMotor motor = especMotorRepository.findById(idVersao).orElse(null);
        EspecTransmissao transmissao = especTransmissaoRepository.findById(idVersao).orElse(null);
        EspecDesempenho desempenho = especDesempenhoRepository.findById(idVersao).orElse(null);
        EspecDimensao dimensao = especDimensaoRepository.findById(idVersao).orElse(null);
        List<FotoVeiculo> fotos = fotoVeiculoRepository.findByVersaoId(idVersao);

        VersaoDetalhesDTO detalhes = new VersaoDetalhesDTO();
        detalhes.setId(versao.getId());
        detalhes.setNome(versao.getNome());
        detalhes.setAnoFabricacao(versao.getAnoFabricacao());
        detalhes.setAnoModelo(versao.getAnoModelo());
        detalhes.setPreco(versao.getPreco());
        detalhes.setNotaMedia(versao.getNotaMedia());
        detalhes.setQtdAvaliacoes(versao.getQtdAvaliacoes());

        detalhes.setNomeModelo(versao.getModelo().getNome());
        detalhes.setNomeMarca(versao.getModelo().getMarca().getNome());

        if (motor != null) {
            detalhes.setMotor(convertMotorToDTO(motor));
        }
        if (transmissao != null) {
            detalhes.setTransmissao(convertTransmissaoToDTO(transmissao));
        }
        if (desempenho != null) {
            detalhes.setDesempenho(convertDesempenhoToDTO(desempenho));
        }
        if (dimensao != null) {
            detalhes.setDimensao(convertDimensaoToDTO(dimensao));
        }

        detalhes.setEquipamentos(obterEquipamentosCategorizados(versao));
        detalhes.setFotos(fotos.stream()
                .map(f -> new FotoVeiculoDTO(f.getUrlFoto(), f.getIsPrincipal()))
                .collect(Collectors.toList()));

        return detalhes;
    }

    public List<VersaoResumoDTO> obterMaisComparados(Long idVersao) {
        return obterDetalhesVersao(idVersao) != null ? versaoRepository.findAll()
                .stream()
                .filter(v -> !v.getId().equals(idVersao))
                .limit(10)
                .map(this::convertToResumoDTO)
                .collect(Collectors.toList()) : List.of();
    }

    public List<Integer> obterAnosFabricacao(Long modeloId) {
        return versaoRepository.findDistinctAnosFabricacaoByModeloId(modeloId);
    }

    public List<Integer> obterAnosModelo(Long modeloId) {
        return versaoRepository.findDistinctAnosModeloByModeloId(modeloId);
    }

    private VersaoResumoDTO convertToResumoDTO(Versao versao) {
        FotoVeiculo fotoPrincipal = fotoVeiculoRepository.findByVersaoIdAndIsPrincipalTrue(versao.getId());

        return new VersaoResumoDTO(
                versao.getId(),
                versao.getNome(),
                versao.getAnoModelo(),
                versao.getPreco(),
                fotoPrincipal != null ? fotoPrincipal.getUrlFoto() : null,
                versao.getModelo().getNome(),
                versao.getModelo().getMarca().getNome());
    }

    private EspecMotorDTO convertMotorToDTO(EspecMotor motor) {
        return new EspecMotorDTO(
                motor.getTipoMotor(),
                motor.getPotenciaCv(),
                motor.getTorqueKgfm(),
                motor.getCombustivel(),
                motor.getValvulas(),
                motor.getConsumoCidade(),
                motor.getConsumoEstrada());
    }

    private EspecTransmissaoDTO convertTransmissaoToDTO(EspecTransmissao transmissao) {
        return new EspecTransmissaoDTO(
                transmissao.getTipoTransmissao(),
                transmissao.getMarchas(),
                transmissao.getTracao(),
                transmissao.getDiferencial());
    }

    private EspecDesempenhoDTO convertDesempenhoToDTO(EspecDesempenho desempenho) {
        return new EspecDesempenhoDTO(
                desempenho.getAceleracao0100(),
                desempenho.getVelocidadeMax(),
                desempenho.getModosConducao());
    }

    private EspecDimensaoDTO convertDimensaoToDTO(EspecDimensao dimensao) {
        return new EspecDimensaoDTO(
                dimensao.getComprimentoMm(),
                dimensao.getLarguraMm(),
                dimensao.getAlturaMm(),
                dimensao.getEntreEixosMm(),
                dimensao.getPesoKg(),
                dimensao.getCapacidadeCarga());
    }

    private EquipamentosCategoriadosDTO obterEquipamentosCategorizados(Versao versao) {
        Map<String, List<EquipamentoDTO>> equipamentosPorCategoria = versao.getEquipamentos()
                .stream()
                .collect(Collectors.groupingBy(
                        equip -> equip.getCategoria().getNome(),
                        Collectors.mapping(
                                equip -> new EquipamentoDTO(
                                        equip.getId(),
                                        equip.getDescricao(),
                                        equip.getCategoria().getNome()),
                                Collectors.toList())));

        return new EquipamentosCategoriadosDTO(equipamentosPorCategoria);
    }
}

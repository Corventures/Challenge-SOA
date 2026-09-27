package br.com.blindspot.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.blindspot.api.dto.response.MarcaResponseDTO;
import br.com.blindspot.api.dto.response.ModeloResponseDTO;
import br.com.blindspot.api.service.MarcaService;
import br.com.blindspot.api.service.ModeloService;

@ExtendWith(MockitoExtension.class)
class MarcaControllerTest {

        @Mock
        private MarcaService marcaService;

        @Mock
        private ModeloService modeloService;

        @InjectMocks
        private MarcaController marcaController;

        @Test
        void obterTodasMarcasDeveRetornarListaPadraoQuandoNaoInformadoPopulares() {
                when(marcaService.obterTodasMarcas()).thenReturn(List.of(
                                new MarcaResponseDTO(1L, "Marca A", "logo-a.png")));

                ResponseEntity<List<MarcaResponseDTO>> response = marcaController.obterTodasMarcas(null);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(1);
                assertThat(response.getBody().get(0).getNome()).isEqualTo("Marca A");
                verify(marcaService).obterTodasMarcas();
        }

        @Test
        void obterTodasMarcasDeveRetornarPopularesQuandoParametroForTrue() {
                when(marcaService.obterMarcasPopulares()).thenReturn(List.of(
                                new MarcaResponseDTO(2L, "Marca B", "logo-b.png")));

                ResponseEntity<List<MarcaResponseDTO>> response = marcaController.obterTodasMarcas(Boolean.TRUE);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(1);
                assertThat(response.getBody().get(0).getNome()).isEqualTo("Marca B");
                verify(marcaService).obterMarcasPopulares();
        }

        @Test
        void obterModelosPorMarcaDeveRetornarModelosDaMarca() {
                when(modeloService.obterModelosPorMarca(1L)).thenReturn(List.of(
                                new ModeloResponseDTO(10L, "Modelo X", 1L, "Marca A")));

                ResponseEntity<List<ModeloResponseDTO>> response = marcaController.obterModelosPorMarca(1L);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).hasSize(1);
                assertThat(response.getBody().get(0).getNome()).isEqualTo("Modelo X");
                verify(modeloService).obterModelosPorMarca(1L);
        }
}
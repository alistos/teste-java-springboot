package org.example.unitTests.service;

import org.example.service.processo.ProcessoService;
import org.example.config.db.ProcessoRepository;
import org.example.exception.ResourceNotFoundException;
import org.example.service.processo.entity.Processo;
import org.example.service.processo.entity.Reu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessoServiceTest {

    @Mock
    private ProcessoRepository processoRepository;

    @InjectMocks
    private ProcessoService processoService;

    private Processo processo;

    @BeforeEach
    void setUp() {
        processo = new Processo();
        processo.setId(1L);
        processo.setNumero("12345");
    }

    @Test
    void testSaveProcesso_Success() {
        when(processoRepository.save(any(Processo.class))).thenReturn(processo);
        Processo savedProcesso = processoService.saveProcesso(processo);
        assertNotNull(savedProcesso);
        assertEquals("12345", savedProcesso.getNumero());
    }

    @Test
    void testSaveProcesso_ThrowsException_WhenNumeroIsNull() {
        processo.setNumero(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processoService.saveProcesso(processo);
        });
        assertEquals("Numero não pode ser null", exception.getMessage());
    }

    @Test
    void testSaveProcesso_ThrowsException_WhenNumeroIsDuplicate() {
        when(processoRepository.existsByNumero(processo.getNumero())).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            processoService.saveProcesso(processo);
        });
        assertEquals("Processo já cadastrado", exception.getMessage());
    }

    @Test
    void testGetAllProcessos_ReturnsEmptyList() {
        when(processoRepository.findAll()).thenReturn(Collections.emptyList());
        List<Processo> processos = processoService.getAllProcessos();
        assertTrue(processos.isEmpty());
    }

    @Test
    void testGetProcessoById_Success() {
        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));
        Processo foundProcesso = processoService.getProcessoById(1L);
        assertNotNull(foundProcesso);
        assertEquals("12345", foundProcesso.getNumero());
    }

    @Test
    void testGetProcessoById_ThrowsException_WhenNotFound() {
        when(processoRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            processoService.getProcessoById(1L);
        });
        assertEquals("Processo não encontrado com o seguinte id 1", exception.getMessage());
    }

    @Test
    void testDeleteProcesso_Success() {
        when(processoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(processoRepository).deleteById(1L);
        assertDoesNotThrow(() -> processoService.deleteProcesso(1L));
    }

    @Test
    void testDeleteProcesso_ThrowsException_WhenNotFound() {
        when(processoRepository.existsById(1L)).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            processoService.deleteProcesso(1L);
        });
        assertEquals("Processo não encontrado com o seguinte id: 1", exception.getMessage());
    }

    @Test
    void testAddReuToProcesso_Success() {
        Reu reu = new Reu();
        reu.setId(1L);
        reu.setNome("John Doe");

        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));
        when(processoRepository.save(any(Processo.class))).thenReturn(processo);

        Processo updatedProcesso = processoService.addReuToProcesso(1L, reu);
        assertNotNull(updatedProcesso);
        assertTrue(updatedProcesso.getReus().contains(reu));
    }

    @Test
    void testAddReuToProcesso_ThrowsException_WhenNomeIsNull() {
        Reu reu = new Reu();
        reu.setNome(null);

        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processoService.addReuToProcesso(1L, reu);
        });
        assertEquals("O nome do Reu não pode ser null ou vazio", exception.getMessage());
    }

    @Test
    void testDeleteReuFromProcesso_Success() {
        Reu reu = new Reu();
        reu.setId(1L);
        reu.setNome("John Doe");

        processo.getReus().add(reu);
        reu.setProcesso(processo);

        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));
        when(processoRepository.save(any(Processo.class))).thenReturn(processo);

        assertDoesNotThrow(() -> processoService.deleteReuFromProcesso(1L, 1L));

        verify(processoRepository, times(1)).save(processo);
        assertFalse(processo.getReus().contains(reu));
    }

    @Test
    void testDeleteReuFromProcesso_ThrowsException_WhenReuNotFound() {
        when(processoRepository.findById(1L)).thenReturn(Optional.of(processo));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            processoService.deleteReuFromProcesso(1L, 1L);
        });
        assertEquals("Reu não encontrado neste processo com o seguinte id: 1", exception.getMessage());
    }
}
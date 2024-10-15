package org.example.service.processo;

import org.example.service.processo.entity.Processo;
import org.example.service.processo.entity.Reu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.config.db.ProcessoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.example.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProcessoService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessoService.class);

    @Autowired
    private ProcessoRepository processoRepository;

    @Transactional
    public Processo saveProcesso(Processo processo) {
        logger.info("Salvando processo com numero: {}", processo.getNumero());
        if (processoRepository.existsByNumero(processo.getNumero())) {
            logger.error("Processo com o seguinte numero ja existe: {}", processo.getNumero());
            throw new RuntimeException("Processo já cadastrado");
        }
        if (processo.getNumero() == null) {
            logger.error("Numero do processo não pode ser null");
            throw new IllegalArgumentException("Numero não pode ser null");
        }
        return processoRepository.save(processo);
    }

    @Transactional
    public List<Processo> saveMultipleProcessos(List<Processo> processos) {
        logger.info("Salvando múltiplos processos");
        for (Processo processo : processos) {
            if (processoRepository.existsByNumero(processo.getNumero())) {
                logger.error("Processo com o seguinte numero ja existe: {}", processo.getNumero());
                throw new RuntimeException("Processo já cadastrado");
            }
            if (processo.getNumero() == null) {
                logger.error("Numero do processo não pode ser null");
                throw new IllegalArgumentException("Numero não pode ser null");
            }
        }
        return processoRepository.saveAll(processos);
    }

    public List<Processo> getAllProcessos() {
        logger.info("Buscando todos os processos");
        return processoRepository.findAll();
    }

    public Processo getProcessoById(Long id) {
        logger.info("Buscando processo com id: {}", id);
        return processoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Processo não encontrado com o seguinte id " + id));
    }

    public void deleteProcesso(Long id) {
        logger.info("Deletando processo com id: {}", id);
        if (!processoRepository.existsById(id)) {
            logger.error("Processo não encontrado com o seguinte id: {}", id);
            throw new ResourceNotFoundException("Processo não encontrado com o seguinte id: " + id);
        }
        processoRepository.deleteById(id);
    }

    @Transactional
    public void deleteMultipleProcessos(List<Long> ids) {
        logger.info("Deletando múltiplos processos");
        for (Long id : ids) {
            if (!processoRepository.existsById(id)) {
                logger.error("Processo nao encontrado com o seguinte id: {}", id);
                throw new ResourceNotFoundException("Processo nao encontrado com o seguinte id " + id);
            }
        }
        processoRepository.deleteAllById(ids);
    }

    public Processo addReuToProcesso(Long processoId, Reu reu) {
        logger.info("Adicionando Reu ao processo com id: {}", processoId);
        Processo processo = processoRepository.findById(processoId)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));
        if (reu.getNome() == null || reu.getNome().isEmpty()) {
            logger.error("O nome do Reu não pode ser null ou vazio");
            throw new IllegalArgumentException("O nome do Reu não pode ser null ou vazio");
        }
        reu.setProcesso(processo);
        processo.getReus().add(reu);
        return processoRepository.save(processo);
    }

    public void deleteReuFromProcesso(Long processoId, Long reuId) {
        logger.info("Deletando Reu com id: {} do processo com id: {}", reuId, processoId);
        Processo processo = processoRepository.findById(processoId)
                .orElseThrow(() -> new ResourceNotFoundException("Processo não encontrado"));
        Reu reuToRemove = processo.getReus().stream()
                .filter(reu -> reu.getId().equals(reuId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Reu não encontrado neste processo com o seguinte id: " + reuId));
        if (!reuToRemove.getProcesso().getId().equals(processoId)) {
            logger.error("Reu não pertence ao Processo dado");
            throw new IllegalArgumentException("Reu does not belong to the given Processo");
        }
        processo.getReus().remove(reuToRemove);
        processoRepository.save(processo);
    }
}
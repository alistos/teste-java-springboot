package org.example.controller.processo;

import org.example.service.processo.ProcessoService;
import org.example.service.processo.entity.Processo;
import org.example.service.processo.entity.Reu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessoController.class);

    @Autowired
    private ProcessoService processoService;

    @PostMapping
    public ResponseEntity<Processo> createProcesso(@RequestBody Processo processo) {
        logger.info("Recebendo requisição para criar processo com numero: {}", processo.getNumero());
        return ResponseEntity.ok(processoService.saveProcesso(processo));
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Processo>> createMultipleProcessos(@RequestBody List<Processo> processos) {
        logger.info("Recebendo requisição para criar múltiplos processos");
        return ResponseEntity.ok(processoService.saveMultipleProcessos(processos));
    }

    @GetMapping
    public ResponseEntity<List<Processo>> getAllProcessos() {
        logger.info("Buscando todos os processos");
        return ResponseEntity.ok(processoService.getAllProcessos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Processo> getProcessoById(@PathVariable Long id) {
        logger.info("Buscando processo com id: {}", id);
        Processo processo = processoService.getProcessoById(id);
        return ResponseEntity.ok(processo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcesso(@PathVariable Long id) {
        logger.info("Deletando processo com id: {}", id);
        processoService.deleteProcesso(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/multiple")
    public ResponseEntity<Void> deleteMultipleProcessos(@RequestBody List<Long> ids) {
        logger.info("Deletando múltiplos processos");
        processoService.deleteMultipleProcessos(ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reus")
    public ResponseEntity<Processo> addReuToProcesso(@PathVariable Long id, @RequestBody Reu reu) {
        logger.info("Adicionando réu ao processo com id: {}", id);
        return ResponseEntity.ok(processoService.addReuToProcesso(id, reu));
    }

    @DeleteMapping("/{processoId}/reus/{reuId}")
    public ResponseEntity<Void> deleteReuFromProcesso(@PathVariable Long processoId, @PathVariable Long reuId) {
        logger.info("Deletando réu com id: {} do processo com id: {}", reuId, processoId);
        processoService.deleteReuFromProcesso(processoId, reuId);
        return ResponseEntity.noContent().build();
    }
}
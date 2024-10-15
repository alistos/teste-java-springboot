package org.example.config.db;

import org.example.service.processo.entity.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    boolean existsByNumero(String numero);
}
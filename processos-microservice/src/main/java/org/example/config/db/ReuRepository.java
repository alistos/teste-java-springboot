package org.example.config.db;

import org.example.service.processo.entity.Reu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReuRepository extends JpaRepository<Reu, Long> {
}
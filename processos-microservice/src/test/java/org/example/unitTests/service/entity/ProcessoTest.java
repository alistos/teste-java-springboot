package org.example.unitTests.service.entity;

import org.example.service.processo.entity.Processo;
import org.example.service.processo.entity.Reu;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class ProcessoTest {

    @Test
    public void testProcessoGettersAndSetters() {
        Processo processo = new Processo();
        processo.setId(1L);
        processo.setNumero("12345");

        assertEquals(1L, processo.getId());
        assertEquals("12345", processo.getNumero());
    }

    @Test
    public void testProcessoReus() {
        Processo processo = new Processo();
        Reu reu = new Reu();
        reu.setId(1L);
        reu.setNome("John Doe");

        processo.getReus().add(reu);

        assertEquals(1, processo.getReus().size());
        Reu retrievedReu = processo.getReus().iterator().next();
        assertEquals("John Doe", retrievedReu.getNome());
    }
}
package org.example.unitTests.service.entity;

import org.example.service.processo.entity.Reu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReuTest {

    @Test
    public void testReuGettersAndSetters() {
        Reu reu = new Reu();
        reu.setId(1L);
        reu.setNome("John Doe");

        assertEquals(1L, reu.getId());
        assertEquals("John Doe", reu.getNome());
    }
}

package org.example.unitTests.controller;

import org.example.controller.processo.ProcessoController;
import org.example.service.processo.ProcessoService;
import org.example.service.processo.entity.Processo;
import org.example.service.processo.entity.Reu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProcessoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessoService processoService;

    @Test
    public void testCreateProcesso() throws Exception {
        Processo processo = new Processo();
        processo.setId(1L);
        processo.setNumero("12345");

        when(processoService.saveProcesso(any(Processo.class))).thenReturn(processo);

        mockMvc.perform(post("/api/processos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"numero\":\"12345\"}"));
    }

    @Test
    public void testCreateProcesso_InvalidJson() throws Exception {
        mockMvc.perform(post("/api/processos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalidJson}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateMultipleProcessos() throws Exception {
        Processo processo1 = new Processo();
        processo1.setId(1L);
        processo1.setNumero("12345");

        Processo processo2 = new Processo();
        processo2.setId(2L);
        processo2.setNumero("67890");

        List<Processo> processos = Arrays.asList(processo1, processo2);

        when(processoService.saveMultipleProcessos(anyList())).thenReturn(processos);

        mockMvc.perform(post("/api/processos/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"numero\":\"12345\"}, {\"numero\":\"67890\"}]"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"numero\":\"12345\"}, {\"id\":2,\"numero\":\"67890\"}]"));
    }

    @Test
    public void testGetAllProcessos() throws Exception {
        Processo processo1 = new Processo();
        processo1.setId(1L);
        processo1.setNumero("12345");

        Processo processo2 = new Processo();
        processo2.setId(2L);
        processo2.setNumero("67890");

        List<Processo> processos = Arrays.asList(processo1, processo2);

        when(processoService.getAllProcessos()).thenReturn(processos);

        mockMvc.perform(get("/api/processos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"numero\":\"12345\"}, {\"id\":2,\"numero\":\"67890\"}]"));
    }

    @Test
    public void testGetAllProcessos_EmptyList() throws Exception {
        when(processoService.getAllProcessos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/processos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testGetProcessoById() throws Exception {
        Processo processo = new Processo();
        processo.setId(1L);
        processo.setNumero("12345");

        when(processoService.getProcessoById(1L)).thenReturn(processo);

        mockMvc.perform(get("/api/processos/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"numero\":\"12345\"}"));
    }

    @Test
    public void testDeleteProcesso() throws Exception {
        doNothing().when(processoService).deleteProcesso(1L);

        mockMvc.perform(delete("/api/processos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteMultipleProcessos() throws Exception {
        doNothing().when(processoService).deleteMultipleProcessos(anyList());

        mockMvc.perform(delete("/api/processos/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2]"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddReuToProcesso() throws Exception {
        Processo processo = new Processo();
        processo.setId(1L);
        processo.setNumero("12345");

        Reu reu = new Reu();
        reu.setId(1L);
        reu.setNome("John Doe");

        processo.getReus().add(reu);

        when(processoService.addReuToProcesso(eq(1L), any(Reu.class))).thenReturn(processo);

        mockMvc.perform(post("/api/processos/1/reus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"numero\":\"12345\",\"reus\":[{\"id\":1,\"nome\":\"John Doe\"}]}"));
    }

    @Test
    public void testDeleteReuFromProcesso() throws Exception {
        doNothing().when(processoService).deleteReuFromProcesso(1L, 1L);

        mockMvc.perform(delete("/api/processos/1/reus/1"))
                .andExpect(status().isNoContent());
    }
}
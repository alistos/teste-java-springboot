package org.example.e2eTests.controller;

import org.example.ProcessosMicroserviceApplication;
import org.example.config.db.ReuRepository;
import org.example.service.processo.entity.Processo;
import org.example.service.processo.entity.Reu;
import org.example.config.db.ProcessoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProcessosMicroserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProcessoControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ReuRepository reuRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/processos";
        processoRepository.deleteAll();
        reuRepository.deleteAll();
    }

    @Test
    void testCreateProcesso() {
        Processo processo = new Processo();
        processo.setNumero("12345");

        ResponseEntity<Processo> response = restTemplate.postForEntity(baseUrl, processo, Processo.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("12345", response.getBody().getNumero());
    }

    @Test
    void testCreateMultipleProcessos() {
        Processo processo1 = new Processo();
        processo1.setNumero("12345");

        Processo processo2 = new Processo();
        processo2.setNumero("67890");

        ResponseEntity<Processo[]> response = restTemplate.postForEntity(baseUrl + "/multiple", List.of(processo1, processo2), Processo[].class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testGetAllProcessos() {
        Processo processo = new Processo();
        processo.setNumero("12345");
        processoRepository.save(processo);

        Processo processo2 = new Processo();
        processo2.setNumero("67890");
        processoRepository.save(processo2);

        ResponseEntity<Processo[]> response = restTemplate.getForEntity(baseUrl, Processo[].class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        assertEquals("12345", response.getBody()[0].getNumero());
    }

    @Test
    void testGetProcessoById() {
        Processo processo = new Processo();
        processo.setNumero("12345");
        processo = processoRepository.save(processo);

        ResponseEntity<Processo> response = restTemplate.getForEntity(baseUrl + "/" + processo.getId(), Processo.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("12345", response.getBody().getNumero());
    }

    @Test
    void testDeleteProcesso() {
        Processo processo = new Processo();
        processo.setNumero("12345");
        processo = processoRepository.save(processo);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/" + processo.getId(), HttpMethod.DELETE, requestEntity, Void.class);

        assertEquals(204, response.getStatusCodeValue());
        assertFalse(processoRepository.existsById(processo.getId()));
    }

    @Test
    void testDeleteMultipleProcessos() {
        Processo processo1 = new Processo();
        processo1.setNumero("12345");
        processo1 = processoRepository.save(processo1);

        Processo processo2 = new Processo();
        processo2.setNumero("67890");
        processo2 = processoRepository.save(processo2);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(List.of(processo1.getId(), processo2.getId()), headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/multiple", HttpMethod.DELETE, requestEntity, Void.class);

        assertEquals(204, response.getStatusCodeValue());
        assertFalse(processoRepository.existsById(processo1.getId()));
        assertFalse(processoRepository.existsById(processo2.getId()));
    }

    @Test
    void testAddReuToProcesso() {
        Processo processo = new Processo();
        processo.setNumero("12345");
        processo = processoRepository.save(processo);

        Reu reu = new Reu();
        reu.setNome("John Doe");

        ResponseEntity<Processo> response = restTemplate.postForEntity(baseUrl + "/" + processo.getId() + "/reus", reu, Processo.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getReus().stream().anyMatch(r -> "John Doe".equals(r.getNome())));
    }

    @Test
    void testDeleteReuFromProcesso() {
        Processo processo = new Processo();
        processo.setNumero("12345");
        processo = processoRepository.save(processo);

        Reu reu = new Reu();
        reu.setNome("John Doe");
        reu.setProcesso(processo);
        reu = reuRepository.save(reu);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/" + processo.getId() + "/reus/" + reu.getId(), HttpMethod.DELETE, requestEntity, Void.class);

        assertEquals(204, response.getStatusCodeValue());
        assertFalse(reuRepository.existsById(reu.getId()));
    }
}
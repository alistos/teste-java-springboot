package org.example.service.processo.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "processo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("numero")
    @Column(unique = true, nullable = false)
    private String numero;

    @JsonManagedReference
    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reu> reus = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Set<Reu> getReus() {
        return reus;
    }

    public void setReus(Set<Reu> reus) {
        this.reus = reus;
    }
}
package com.example.Gestione_eventi_con_prenotazione.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titolo;
    private String descrizione;
    private LocalDate dataEvento;
    private String luogo;
    private int postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "creatore_id")
    private User creatore;

    @OneToMany(mappedBy = "evento")
    private List<Prenotazione> prenotazioni;
}

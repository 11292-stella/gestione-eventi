package com.example.Gestione_eventi_con_prenotazione.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventoDto {

    @NotEmpty(message = "Il titolo non può essere vuoto")
    private String titolo;

    private String descrizione;

    @NotNull(message = "la data dell'evento è obbligatoria")
    private LocalDate dataEvento;

    @NotEmpty(message = "Il luogo non può essere vuoto")
    private String luogo;

    @NotNull(message = "Il numero di posti disponibili è obbligatorio")
    private Integer postiDisponibili;
}

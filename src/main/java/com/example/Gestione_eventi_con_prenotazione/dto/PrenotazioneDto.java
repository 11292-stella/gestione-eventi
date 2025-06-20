package com.example.Gestione_eventi_con_prenotazione.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrenotazioneDto {

    @NotNull(message = "Id utente obbligatorio")
    private int userId;

    @NotNull(message = "Id evento obbligatorio")
    private int eventoId;
}

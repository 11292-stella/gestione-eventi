package com.example.Gestione_eventi_con_prenotazione.repository;

import com.example.Gestione_eventi_con_prenotazione.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione,Integer> {

    List<Prenotazione> findByUtenteId(int utenteId);
}

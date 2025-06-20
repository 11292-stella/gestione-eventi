package com.example.Gestione_eventi_con_prenotazione.repository;

import com.example.Gestione_eventi_con_prenotazione.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento,Integer> {

    List<Evento> findByCreatoreId(int creatoreId);
}

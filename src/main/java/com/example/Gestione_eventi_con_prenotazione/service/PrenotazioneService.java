package com.example.Gestione_eventi_con_prenotazione.service;

import com.example.Gestione_eventi_con_prenotazione.dto.PrenotazioneDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.model.Evento;
import com.example.Gestione_eventi_con_prenotazione.model.Prenotazione;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.repository.EventoRepository;
import com.example.Gestione_eventi_con_prenotazione.repository.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventoRepository eventoRepository;

    public Prenotazione savePrenotazione(PrenotazioneDto prenotazioneDto, User authenticatedUser) throws NotFoundException {

        User user = userService.getUser(prenotazioneDto.getEventoId());

        Evento evento = eventoRepository.findById(prenotazioneDto.getEventoId())
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));
        Prenotazione prenotazione = new Prenotazione();

        prenotazione.setDataPrenotazione(LocalDateTime.now());
        prenotazione.setUtente(user);
        prenotazione.setEvento(evento);

        return prenotazioneRepository.save(prenotazione);



    }

    public Page<Prenotazione> getAllPrenotazioni(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione getPrenotazione(int id)throws NotFoundException{
        return prenotazioneRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Prenotazione con id" + id + "non presente"));
    }

    public Prenotazione updatePrenotazione(int id, PrenotazioneDto prenotazioneDto) throws NotFoundException {
        Prenotazione prenotazioneDaAggiornare = getPrenotazione(id);

        if (prenotazioneDaAggiornare.getEvento().getId()!= (prenotazioneDto.getEventoId())) {
            Evento evento = eventoRepository.findById(prenotazioneDto.getEventoId())
                    .orElseThrow(() -> new NotFoundException("Evento non trovato"));
            prenotazioneDaAggiornare.setEvento(evento);
        }

        if (prenotazioneDaAggiornare.getUtente().getId() != prenotazioneDto.getUserId()) {
            User utente = userService.getUser(prenotazioneDto.getUserId());
            prenotazioneDaAggiornare.setUtente(utente);
        }
        return prenotazioneRepository.save(prenotazioneDaAggiornare);
    }

    public void deletePrenotazione(int id)throws NotFoundException{
        Prenotazione prenotazioneDaCancellare = getPrenotazione(id);

        prenotazioneRepository.delete(prenotazioneDaCancellare);

    }

}

package com.example.Gestione_eventi_con_prenotazione.service;

import com.example.Gestione_eventi_con_prenotazione.dto.EventoDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.model.Evento;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private UserService userService;

    public Evento saveEvento(EventoDto eventoDto, User creatore) throws NotFoundException {




        Evento evento = new Evento();
        evento.setTitolo(eventoDto.getTitolo());
        evento.setLuogo(eventoDto.getLuogo());
        evento.setDescrizione(eventoDto.getDescrizione());
        evento.setPostiDisponibili(eventoDto.getPostiDisponibili());
        evento.setDataEvento(eventoDto.getDataEvento());
        evento.setCreatore(creatore);
        return eventoRepository.save(evento);

    }

    public Page<Evento> getAllEventi(int page, int size, String sortBy ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventoRepository.findAll(pageable);
    }

    public Evento getEvento(int id) throws NotFoundException {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento con id" + id + "non trovato"));
    }

    public Evento updateEvento(int id, EventoDto eventoDto) throws NotFoundException {
        Evento eventoDaAggiornare = getEvento(id);

        eventoDaAggiornare.setTitolo(eventoDto.getTitolo());
        eventoDaAggiornare.setLuogo(eventoDto.getLuogo());
        eventoDaAggiornare.setPostiDisponibili(eventoDto.getPostiDisponibili());
        eventoDaAggiornare.setDescrizione(eventoDto.getDescrizione());
        eventoDaAggiornare.setDataEvento(eventoDto.getDataEvento());


        return eventoRepository.save(eventoDaAggiornare);
    }

    public void deleteEvento(int id) throws NotFoundException {
        Evento eventoDaCancellare = getEvento(id);
        eventoRepository.delete(eventoDaCancellare);
    }




}

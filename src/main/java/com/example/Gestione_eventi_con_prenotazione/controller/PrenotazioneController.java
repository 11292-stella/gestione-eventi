package com.example.Gestione_eventi_con_prenotazione.controller;

import com.example.Gestione_eventi_con_prenotazione.dto.EventoDto;
import com.example.Gestione_eventi_con_prenotazione.dto.PrenotazioneDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.exception.ValidationException;
import com.example.Gestione_eventi_con_prenotazione.model.Evento;
import com.example.Gestione_eventi_con_prenotazione.model.Prenotazione;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE') ")
    public Prenotazione savePrenotazione(@RequestBody @Validated PrenotazioneDto prenotazioneDto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal User authenticatedUser) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors()
                    .stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (e, s) -> e + s));
        }

        return prenotazioneService.savePrenotazione(prenotazioneDto, authenticatedUser);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE') ")
    public Page<Prenotazione> getAllPrenotazioni(@RequestParam(defaultValue = "0")int page,
                                     @RequestParam(defaultValue = "5")int size,
                                     @RequestParam(defaultValue = "id")String sortBy){
        return prenotazioneService.getAllPrenotazioni(page,size,sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE')")
    public Prenotazione getPrenotazioni(@PathVariable int id) throws NotFoundException {
        return prenotazioneService.getPrenotazione(id);
    }

    @PutMapping("/{id}")
    public Prenotazione updatePrenotazione(@PathVariable int id, @RequestBody @Validated PrenotazioneDto prenotazioneDto,BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors()
                    .stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }

        return prenotazioneService.updatePrenotazione(id,prenotazioneDto);
    }

    @DeleteMapping("/{id}")
    public void deletePrenotazione(@PathVariable int id) throws NotFoundException {
        prenotazioneService.deletePrenotazione(id);
    }
}

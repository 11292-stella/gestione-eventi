package com.example.Gestione_eventi_con_prenotazione.controller;


import com.example.Gestione_eventi_con_prenotazione.dto.EventoDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.exception.ValidationException;
import com.example.Gestione_eventi_con_prenotazione.model.Evento;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;


    @PostMapping("")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento saveEvento(@RequestBody @Validated EventoDto eventoDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User authenticatedUser) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors()
                    .stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (e, s) -> e + s));
        }

        return eventoService.saveEvento(eventoDto, authenticatedUser);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'UTENTE') ")
    public Page<Evento>getAllEventi(@RequestParam(defaultValue = "0")int page,
                                    @RequestParam(defaultValue = "5")int size,
                                    @RequestParam(defaultValue = "id")String sortBy){
        return eventoService.getAllEventi(page,size,sortBy);
    }

    @GetMapping("/{id}")
    public Evento getEvento(@PathVariable int id) throws NotFoundException {
        return eventoService.getEvento(id);
    }

    @PutMapping("/{id}")
    public Evento updateEvento(@PathVariable int id, @RequestBody @Validated EventoDto eventoDto,BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors()
                    .stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }

        return eventoService.updateEvento(id,eventoDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvento(@PathVariable int id) throws NotFoundException {
        eventoService.deleteEvento(id);
    }

}

package com.example.Gestione_eventi_con_prenotazione.controller;


import com.example.Gestione_eventi_con_prenotazione.dto.UserDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.exception.ValidationException;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('UTENTE', 'ORGANIZZATORE')")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UTENTE', 'ORGANIZZATORE')")
    public User getUserById(@PathVariable int id, @AuthenticationPrincipal User authenticatedUser) throws NotFoundException {
        if (authenticatedUser.getId() != id && !authenticatedUser.getRole().name().equals("ORGANIZZATORE")) {
            throw new AccessDeniedException("Non autorizzato a visualizzare questo profilo.");
        }
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UTENTE', 'ORGANIZZATORE')")
    public User updateUser(@PathVariable int id,
                           @RequestBody @Validated UserDto userDto,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal User authenticatedUser)
            throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(" "));
            throw new ValidationException(errorMessage.trim());
        }

        if (authenticatedUser.getId() != id && !authenticatedUser.getRole().name().equals("ORGANIZZATORE")) {
            throw new AccessDeniedException("Non autorizzato a modificare questo profilo.");
        }

        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UTENTE', 'ORGANIZZATORE')")
    public String deleteUser(@PathVariable int id, @AuthenticationPrincipal User authenticatedUser) throws NotFoundException {
        if (authenticatedUser.getId() != id && !authenticatedUser.getRole().name().equals("ORGANIZZATORE")) {
            throw new AccessDeniedException("Non autorizzato ad eliminare questo profilo.");
        }

        userService.deleteUser(id);
        return "User con ID " + id + " eliminato con successo";
    }


}

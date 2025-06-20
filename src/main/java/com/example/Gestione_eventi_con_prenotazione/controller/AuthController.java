package com.example.Gestione_eventi_con_prenotazione.controller;


import com.example.Gestione_eventi_con_prenotazione.dto.LoginDto;
import com.example.Gestione_eventi_con_prenotazione.dto.UserDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.exception.ValidationException;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.service.AuthService;
import com.example.Gestione_eventi_con_prenotazione.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (s, e) -> s + e + " "); // Added space for readability
            throw new ValidationException(errorMessage.trim());
        }
        return userService.saveUser(userDto);
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (s, e) -> s + e + " "); // Added space for readability
            throw new ValidationException(errorMessage.trim());
        }
        return authService.login(loginDto);
    }
}

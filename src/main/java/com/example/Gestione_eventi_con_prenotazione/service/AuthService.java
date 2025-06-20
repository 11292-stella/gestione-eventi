package com.example.Gestione_eventi_con_prenotazione.service;


import com.example.Gestione_eventi_con_prenotazione.dto.LoginDto;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.repository.UserRepository;
import com.example.Gestione_eventi_con_prenotazione.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new NotFoundException("Credenziali non valide. Utente non trovato o password errata."));

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {

            return jwtTool.createToken(user);
        } else {
            throw new NotFoundException("Credenziali non valide. Utente non trovato o password errata.");
        }
    }
}

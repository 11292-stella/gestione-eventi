package com.example.Gestione_eventi_con_prenotazione.service;


import com.example.Gestione_eventi_con_prenotazione.dto.UserDto;
import com.example.Gestione_eventi_con_prenotazione.enumeration.Role;
import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.exception.ValidationException;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import com.example.Gestione_eventi_con_prenotazione.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    public User saveUser(UserDto userDto) throws ValidationException {
        User user = new User();
        user.setNome(userDto.getNome());
        user.setCognome(userDto.getCognome());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setEmail(userDto.getEmail());

        if (userDto.getRole() != null) {
            try {
                user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Ruolo non valido. I ruoli ammessi sono: UTENTE, ORGANIZZATORE.");
            }
        } else {
            user.setRole(Role.UTENTE); // default se assente
        }

        User savedUser = userRepository.save(user);
        sendRegistrationEmail(savedUser.getEmail());
        return savedUser;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUser(int id) throws NotFoundException {
        return userRepository.findById(id).
                orElseThrow(() -> new NotFoundException("User con id " + id + " non trovato"));
    }



    public User updateUser(int id, UserDto userDto) throws NotFoundException {
        User userDaAggiornare = getUser(id);

        userDaAggiornare.setNome(userDto.getNome());
        userDaAggiornare.setCognome(userDto.getCognome());
        userDaAggiornare.setUsername(userDto.getUsername());
        if (!passwordEncoder.matches(userDto.getPassword(), userDaAggiornare.getPassword())) {
            userDaAggiornare.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(userDaAggiornare);
    }

    public void deleteUser(int id) throws NotFoundException {
        User userDaCancellare = getUser(id);
        userRepository.delete(userDaCancellare);
    }

    private void sendRegistrationEmail(String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail); 
        message.setSubject("Benvenuto!");
        message.setText("Congratulazioni! La tua registrazione al servizio è avvenuta con successo.\n" +
                "Ora puoi accedere e utilizzare tutte le nostre funzionalità.\n\n" +
                "Cordiali saluti!");

        try {
            javaMailSender.send(message);
            System.out.println("Email di registrazione inviata a: " + recipientEmail);
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email di registrazione a " + recipientEmail + ": " + e.getMessage());
           
        }
    }
    }



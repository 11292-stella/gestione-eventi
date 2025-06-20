package com.example.Gestione_eventi_con_prenotazione.repository;


import com.example.Gestione_eventi_con_prenotazione.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public Optional<User> findByUsername(String username);
}

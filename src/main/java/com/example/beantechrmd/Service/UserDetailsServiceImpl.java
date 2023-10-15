package com.example.beantechrmd.Service;

import com.example.beantechrmd.Entities.UserApp;
import com.example.beantechrmd.Models.UserDetailsImpl;
import com.example.beantechrmd.Repositories.UserAppRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//Abbiamo bisogno dello UserDetailsService per poter creare lo UserDetails, partendo da uno AppUser
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserAppRepository userRepository;

    //Metodo UserDetailsService
    @Override
    @Transactional //Per ovviare a problematiche di alterazione del db
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApp user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con il nome: " + username));

        return UserDetailsImpl.build(user);
    }
}

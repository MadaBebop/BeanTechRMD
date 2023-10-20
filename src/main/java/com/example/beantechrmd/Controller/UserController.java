package com.example.beantechrmd.Controller;

import com.example.beantechrmd.Entity.Role;
import com.example.beantechrmd.Entity.UserApp;
import com.example.beantechrmd.Pojo.Responses.UserAppInfoResponse;
import com.example.beantechrmd.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
controllerUser: dovrebbe permettere allo user di vedere e modificare SOLO il proprio profilo:
    problema:
    Un qualsiasi utente con autenticazione di livello user potrebbe visualizzare altri utenti
    attraverso il metodo "getProfile", gli basterebbe inserire l'id che preferisce
    la logica che -> dato un token questo identifichi uno e uno solo utente e gli permetta solo di vedere il proprio account
    non ho tempo di implementarla, ma sarebbe la best practise
    stesso problema per gli altri metodi del controllerUser
 */

@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/user")
@RestController
@PreAuthorize("hasAnyRole()")
public class UserController {

    UserService userService;

    //Visualizzare se stessò
    @GetMapping("/profile")
    public UserAppInfoResponse getProfileName(HttpServletRequest request) {
        UserApp userFound = userService.getUserByToken(request);

        return new UserAppInfoResponse(
                userFound.getId(),
                userFound.getUsername(),
                userFound.getRoles().stream()
                        .map(Role::toString)
                        .toList()
        );
    }


}

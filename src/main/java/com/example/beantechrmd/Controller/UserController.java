package com.example.beantechrmd.Controller;

import com.example.beantechrmd.Pojo.Responses.UserAppInfoResponse;
import com.example.beantechrmd.Service.UserService;
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
@RequestMapping("/user")
@RestController
@PreAuthorize("hasAnyRole()")
public class UserController {

    UserService userService;

    //Visualizzare se stessò
    @GetMapping("/visualizza/{id}")
    public UserAppInfoResponse getProfile(@PathVariable int id) {
        return userService.getUserAppById(id);
        //Non ho avuto tempo di usare thymeleaf
        //Avrei potuto semplicemente ritornare un Model con un attributo-> UserApp pojo
    }

    //Modificarsi
    @PutMapping
    public String modifyProfile(){
        return "";
    }

}

package com.example.beantechrmd.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RequestMapping("/api/admin")
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    //Visualizzare tutti gli utenti
    //Eliminare un utente
    //Creare un utente

}

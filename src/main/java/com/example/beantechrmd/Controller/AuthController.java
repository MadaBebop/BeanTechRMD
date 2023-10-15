package com.example.beantechrmd.Controller;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.beantechrmd.Config.Security.Jwt.JwtUtils;
import com.example.beantechrmd.Entity.Role;
import com.example.beantechrmd.Entity.UserApp;
import com.example.beantechrmd.Model.EnumRole;
import com.example.beantechrmd.Model.UserDetailsImpl;
import com.example.beantechrmd.Pojo.Requests.LoginRequest;
import com.example.beantechrmd.Pojo.Requests.SingupRequest;
import com.example.beantechrmd.Pojo.Responses.MessageResponse;
import com.example.beantechrmd.Pojo.Responses.UserAppInfoResponse;
import com.example.beantechrmd.Repository.RoleRepository;
import com.example.beantechrmd.Repository.UserAppRepository;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    UserAppRepository userRepository;
    RoleRepository roleRepository;
    JwtUtils jwtUtils;
    PasswordEncoder encoder;
    AuthenticationManager authenticationManager;


    //login
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );

        //Creazione del token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetailsImpl);

        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserAppInfoResponse(
                        userDetailsImpl.getId(),
                        userDetailsImpl.getUsername(),
                        roles)
                );
    }


    //Iscrizione
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SingupRequest signUpRequest) {

        //Controllo esistenza dell'utente
        if (userRepository.existsByUsername(signUpRequest.username())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Nome utente già utilizzato!"));
        }

        // Nuovo utente
        UserApp newUserToCreate = new UserApp(
                signUpRequest.username(),
                encoder.encode(signUpRequest.password())
        );

        //Ruolo del nuovo utente?
        Set<String> strRoles = signUpRequest.roles();
        Set<Role> roles = new HashSet<>();

        //Messaggio di errore per lo switch
        final String errorMessage = "Ruolo non trovato";

        /*  TODO: spostare la logica che lavora con il Role repository in una classe service specifica
            Per il controllo del ruolo dell'utente, sarebbe meglio spostare la logica in un service,
            ma per mancanza di tempo la metto nel controller
         */
        if (strRoles == null) {
            Role userRole = roleRepository.findByAuthority(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(errorMessage));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByAuthority(EnumRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(errorMessage));
                        roles.add(adminRole);
                        break;

                    case "manager":
                        Role managerRole = roleRepository.findByAuthority(EnumRole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(errorMessage));
                        roles.add(managerRole);
                        break;

                    default: //User di default
                        Role userRole = roleRepository.findByAuthority(EnumRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(errorMessage));
                        roles.add(userRole);
                }
            });
        }

        newUserToCreate.setRoles(roles);
        userRepository.save(newUserToCreate);

        return ResponseEntity.ok(new MessageResponse("Utente creato con successo!"));
    }


    //Logout
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("Ti sei disconnesso :,) "));
    }
}
package com.example.beantechrmd.Service;

import com.example.beantechrmd.Config.Security.Jwt.JwtUtils;
import com.example.beantechrmd.Entity.UserApp;
import com.example.beantechrmd.Repository.UserAppRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    UserAppRepository userAppRepository;
    JwtUtils jwtUtils;


    public UserApp getUserByToken (HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        Optional<UserApp> userToFind;

        if(jwtUtils.validateJwtToken(jwt)) {  //Token valido?
            userToFind = userAppRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt));
            return userToFind.orElse(null);
        }else{
            return null;
        }
    }

}

    /*
    //Visualizzare se stessò
    public UserAppInfoResponse getUserAppById(Integer id) {
        if(id == null) throw new IllegalArgumentException("id non può essere nullo");

        UserApp userToVisualize = userAppRepository.findById(id).orElseThrow();

        //Conversione in pojo
        UserAppInfoResponse response = new UserAppInfoResponse(
                userToVisualize.getId(),
                userToVisualize.getUsername(),
                userToVisualize.getRoles()
                        .stream()
                        .map(Role::toString)
                        .collect(Collectors.toList())
                );

        return response;
    }
   PROBLEMA DI SICUREZZA LEGATA AL INPUT: ID
     */


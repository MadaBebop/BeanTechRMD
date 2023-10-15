package com.example.beantechrmd.Service;

import com.example.beantechrmd.Entity.Role;
import com.example.beantechrmd.Entity.UserApp;
import com.example.beantechrmd.Pojo.Responses.UserAppInfoResponse;
import com.example.beantechrmd.Repository.UserAppRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    UserAppRepository userAppRepository;

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

    //Modificarsi


}

package com.example.beantechrmd.Model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.beantechrmd.Entity.UserApp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
    Lo userDetails standard può fornirci solo alcune informazioni in merito allo user, se vogliamo invece
    ottenerne una maggior varietà(Es. email, id o con altri fields che si possono aggiungere in seguito)
    dobbiamo creare una implementazione del userDetails.
    In questa classe stiamo essenzialmente definendo un oggetto della security.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Integer id;
    private String username;
    @JsonIgnore  //Evitiamo che la password venga serializzata
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    //Conversione UserApp -> UserDetails implementato
    public static UserDetailsImpl build(UserApp user) {

        //Conversione del Set<Role> -> List<GrantedAuthority> per poter lavorare con l'oggetto autorità security
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }


    //Metodi UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //Non implementiamo tutti i metodi di stato dell'accounting
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //Equals
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
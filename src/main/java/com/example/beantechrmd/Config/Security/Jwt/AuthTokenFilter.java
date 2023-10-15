package com.example.beantechrmd.Config.Security.Jwt;

import java.io.IOException;

import com.example.beantechrmd.Config.Security.Service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/*
    Creiamo un filtro per le richieste, che sfrutterà il metodo di OnePerRequestFilter
 */
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private UserDetailsServiceImpl userDetailsService;
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);



    //Parsing del jwt
    private String parseJwt(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }

    /*  doFilterInternal:
        1. Prendiamo il token dal cookie (attraverso parsing)
        2. Prendiamo il nome del proprietario del cookie
        3. Creazione di un Authentication
        4. Inserimento del UserDetails nel SecurityContext
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //1.
            String jwt = parseJwt(request);
            if (jwtUtils.validateJwtToken(jwt) && jwt != null) {
                //2.
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //3
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //4
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //NB Potremo successivamente reperire l'oggetto dal SecurityContextHolder
            }
        } catch (Exception e) {
            logger.error("Impossibile impostare l'autenticazione dello user: {e}", e);
        }

        filterChain.doFilter(request, response);
    }
}

package com.example.beantechrmd.Config.Security;

import com.example.beantechrmd.Config.Security.Jwt.AuthTokenFilter;
import com.example.beantechrmd.Config.Security.Service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
//@EnableMethodSecurity
public class SpringSecurityConfig {

    UserDetailsServiceImpl userDetailsService;



        @Bean
         public AuthTokenFilter authenticationJwtTokenFilter() {
            return new AuthTokenFilter();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());

            return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
            return authConfig.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


         @Bean
         public SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {

             https
                     .csrf().disable()  //Sicché usiamo jwt non ci serve csrf
                     .authorizeHttpRequests( req -> req
                             .requestMatchers("/api/auth/**").permitAll()
                             .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "MODERATOR", "USER")
                             .requestMatchers("/api/moderator/**").hasAnyAuthority("ADMIN", "MODERATOR")
                             .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                             .anyRequest().authenticated())
                     .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                     .authenticationProvider(authenticationProvider())
                     .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

             //Login / Logout
             https
                     .formLogin( log-> log.loginPage("/auth/login"))
                     .logout( log-> log.logoutUrl("/auth/logout"));


             return https.build();
          }


}
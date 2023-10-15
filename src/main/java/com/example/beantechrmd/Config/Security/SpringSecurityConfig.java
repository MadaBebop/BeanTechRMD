package com.example.beantechrmd.Config.Security;

import com.example.beantechrmd.Config.Security.Jwt.AuthTokenFilter;
import com.example.beantechrmd.Config.Security.Service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


//@EnableWebSecurity
@AllArgsConstructor
@Configuration
@EnableMethodSecurity //securedEnabled = true , prePostEnabled = true
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
        public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

            MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
            http
                    .authorizeHttpRequests(auth -> auth
                          .requestMatchers(PathRequest.toH2Console()).permitAll()
                          .requestMatchers(mvcMatcherBuilder.pattern("/auth/**")).permitAll()
                          .anyRequest().authenticated()
                  );

             // Le successive istruzioni sono solo per /h2-console
            http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()));
             http.headers().frameOptions().disable();

            //login / logout
             http.formLogin(Customizer.withDefaults())
                     .logout(Customizer.withDefaults());

             return http.build();
        }

}

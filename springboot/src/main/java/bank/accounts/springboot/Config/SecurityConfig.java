package bank.accounts.springboot.Config;

import bank.accounts.springboot.Services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService myUserDetailsService; //Customize it to not use the default, create a Service class and a Repository interface to fetch the data

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //It will return the value of our custom security filter chain

        return http
                .csrf( customizer -> customizer.disable())
                .authorizeHttpRequests( request -> request
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()// This endpoint doeant need authentication
                        .requestMatchers(HttpMethod.GET, "/user/all").hasRole("ADMIN")
                        .anyRequest().authenticated()) //Enable authorization
                .formLogin(Customizer.withDefaults()) //Enable Login Form - Web - If I want STATELESS connection I have to configure it and disable the application form for web
                .httpBasic(Customizer.withDefaults()) //Enable login - Postman
                .build();

    }

    /*@Bean
    public UserDetailsService userDetailsService(){ //Configuring Custom Bean to return users from db
        // Build 2 InMemory users
        UserDetails user1 = User.withDefaultPasswordEncoder() //Implements UserDetails
                .username("Chris")
                .password("Roum123")
                .roles("USER")
                .build();

        UserDetails user2 = User.withDefaultPasswordEncoder() //Implements UserDetails
                .username("Nick")
                .password("Nick123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2); //Implements UserDeatilsManager extends UserDetailsManager

    }*/

    @Bean
    public AuthenticationProvider authenticationProvider(){ //Make Custom Authentication Provider to connect it with db

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12)); //Authenticate using the real password and not the encrypted
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        return authenticationProvider;
    }
}

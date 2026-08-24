package it.uniroma3.it.rez3d.authentication;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;
    
    public SecurityConfiguration(DataSource dataSource){
        this.dataSource = dataSource;
    }

    //serve per trovare i dettagli degli utenti
    @Bean
    public UserDetailsService userDetailsService(){
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
            "SELECT username, password, 1 as enabled FROM credentials WHERE username=?"
        );
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, role FROM credentials WHERE username=?"
        );  
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabilitiamo temporaneamente il CSRF (ci eviterà errori strani quando testerai i form di inserimento)
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(auth -> auth
                //tutti possono caricare il CSS, le immagin 
                .requestMatchers("/css/**","/images/**","/register","/login").permitAll()
                // 1. Diciamo a Spring che l'area ADMIN è blindata
                .requestMatchers("/admin/**").authenticated() 
                
                // 2. Diciamo a Spring che TUTTO il resto del sito è PUBBLICO!
                .anyRequest().permitAll() 
            )
            .formLogin(form -> form
                //diciamo a spring dove andare
                .loginPage("/login")
                //se il login ha successo andiamo alla home /
                .defaultSuccessUrl("/",true)
                //se sbaglia passw
                .failureUrl("/login?error=true")
                .permitAll() // Usa ancora la paginetta brutta di default per ora
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        
        return http.build();
    }

   
}

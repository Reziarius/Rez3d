package it.uniroma3.it.rez3d.authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabilitiamo temporaneamente il CSRF (ci eviterà errori strani quando testerai i form di inserimento)
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(auth -> auth
                // 1. Diciamo a Spring che l'area ADMIN è blindata
                .requestMatchers("/admin/**").authenticated() 
                
                // 2. LA MAGIA: Diciamo a Spring che TUTTO il resto del sito è PUBBLICO!
                // Ora puoi cliccare ovunque senza che ti chieda la password.
                .anyRequest().permitAll() 
            )
            .formLogin(form -> form
                .permitAll() // Usa ancora la paginetta brutta di default per ora
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

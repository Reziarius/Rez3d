package it.uniroma3.it.rez3d.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.it.rez3d.model.Credentials;
import it.uniroma3.it.rez3d.repository.CredentialsRepository;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class CredentialsService {
    private PasswordEncoder passwordEncoder;
    private CredentialsRepository credentialsRepository;

    public CredentialsService (PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository){
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional
    public Credentials getCredentials (Long id){
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials (String username){
        Optional<Credentials> result = Optional.ofNullable(this.credentialsRepository.findByUsername(username));
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials (Credentials credentials){
       credentials.setRole(Credentials.DEFAULT_ROLE);
       credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));;
       return this.credentialsRepository.save(credentials);
    }
}

package it.uniroma3.it.rez3d.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.it.rez3d.model.Credentials;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.repository.CredentialsRepository;
import it.uniroma3.it.rez3d.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;

    public UserService(UserRepository userRepository, CredentialsRepository credentialsRepository){
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional(readOnly = true)
    public User getUser(Long id){
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username){
        if (username == null) {
            return null;
        }
        // Spring Security autentica tramite Credentials: da Credentials risaliamo sempre e in modo pulito all'User associato!
        Credentials creds = this.credentialsRepository.findByUsername(username);
        if (creds != null && creds.getUser() != null) {
            return creds.getUser();
        }
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public java.util.List<User> findAllSorted() {
        return this.userRepository.findByOrderByUsernameAsc();
    }
}

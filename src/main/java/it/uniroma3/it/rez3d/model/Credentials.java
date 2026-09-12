package it.uniroma3.it.rez3d.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Credentials {
    public static final String DEFAULT_ROLE = "DEFAULT";
    public static final String ADMIN_ROLE = "ADMIN";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 20, message = "Lo username deve contenere tra 3 e 20 caratteri")
    @Column(nullable = false, unique = true)
    private String username;
    
    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 4, message = "La password deve contenere almeno 4 caratteri")
    @JsonIgnore
    @Column(nullable = false, unique = false)
    private String password;
    
    private String role;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    
    public static String getDefaultRole() {
        return DEFAULT_ROLE;
    }
    public static String getAdminRole() {
        return ADMIN_ROLE;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Credentials other = (Credentials) obj;
        if (id == null || other.id == null)
            return false;
        return id.equals(other.id);
    }
}

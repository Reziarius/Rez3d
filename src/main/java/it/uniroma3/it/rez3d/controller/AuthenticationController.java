package it.uniroma3.it.rez3d.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.it.rez3d.model.Credentials;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.CredentialsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@Controller
public class AuthenticationController {
    private final CredentialsService credentialsService;

    public AuthenticationController(CredentialsService credentialsService){
        this.credentialsService = credentialsService;
    }

    //mettere value è come non metterlo, però se bisogna passare piu di un parametro è necessario 
    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "authentication/registerUser";
    }

    
    @GetMapping(value = "/login")
    public String showLoginForm(Model model) {
        return "authentication/login";
    }

    @GetMapping(value = "/success")
    public String defaultSuccessLogin(Model model) {
        return "redirect:/";
    }
    
    @PostMapping(value = {"/register"})
    public String registerUser(
                    @Valid @ModelAttribute("user") User user, BindingResult userBindingResult, 
                    @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult) {
        //se va tutto bene (non ci sono errori ne in un user ne in credentials)
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()){
            user.setUsername(credentials.getUsername());
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            return "redirect:/";
        }
        //se ci sono errori rimanda alla pagina di registrazione
        return "authentication/registerUser";
    }
    
}

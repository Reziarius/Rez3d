package it.uniroma3.it.rez3d.controller;

import it.uniroma3.it.rez3d.service.OrderLineService;
import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.RealProduct;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.service.OrderService;
import it.uniroma3.it.rez3d.service.PrintFileService;
import it.uniroma3.it.rez3d.service.RealProductService;
import it.uniroma3.it.rez3d.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RealProductController {
    private final OrderLineService orderLineService;
    private final RealProductService realProductService;
    private final PrintFileService printFileService;
    private final UserService userService;
    private final OrderService orderService;

    public RealProductController(RealProductService realProductService, PrintFileService printFileService,
            UserService userService, OrderService orderService, OrderLineService orderLineService) {
        this.realProductService = realProductService;
        this.printFileService = printFileService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderLineService = orderLineService;
    }

    @GetMapping("/files/{id}/personalizza")
    public String formPersonalizzazione(@PathVariable Long id, Model model) {
        Optional<PrintFile> optionalFile = printFileService.findById(id);
        if (optionalFile.isEmpty()) {
            return "redirect:/files";
        }

        model.addAttribute("file", optionalFile.get());
        model.addAttribute("product", new RealProduct());
        return "products/formPersonalizzaProdotto";
    }

    @PostMapping("/files/{fileId}/personalizza")
    public String salvaProdottoPersonalizzato(@PathVariable("fileId") Long fileId,
                                            @ModelAttribute("product") RealProduct product,
                                            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Optional<PrintFile> optionalFile = printFileService.findById(fileId);
        if (optionalFile.isEmpty()) {
            return "redirect:/files";
        }

        PrintFile file = optionalFile.get();
        String username = principal.getName();
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            return "redirect:/login";
        }

        // recuperiamo carrello
        Order carrello = orderService.getOrCreateCart(loggedUser);

        boolean prodottoTrovato = false;
        if (carrello.getItems() != null) {
            // scorriamo tutte le righe per fare il check
            for (OrderLine line : carrello.getItems()) {
                RealProduct prodottoEsistente = line.getProduct();
                // check
                if (prodottoEsistente != null && prodottoEsistente.getFile() != null &&
                        prodottoEsistente.getFile().getId().equals(file.getId()) &&
                        java.util.Objects.equals(prodottoEsistente.getSize(), product.getSize()) &&
                        java.util.Objects.equals(prodottoEsistente.getDipinto(), product.getDipinto())) {
                    // se trovi che c'è già un prodotto identico nel carrello allora aumenti
                    // soltanto la quantita di quella orderLine
                    line.setQuantity(line.getQuantity() + 1);
                    orderLineService.save(line);
                    prodottoTrovato = true;
                    break;
                }
            }
        }
        if (!prodottoTrovato) {
            realProductService.creaProdotto(product, file);
            // creiamo una nuova orderline
            OrderLine line = new OrderLine();
            line.setOrder(carrello);
            // collego il prodotto alla riga
            line.setProduct(product);
            line.setQuantity(1);

            orderLineService.save(line);
        }
        
        return "redirect:/cart";
    }

}

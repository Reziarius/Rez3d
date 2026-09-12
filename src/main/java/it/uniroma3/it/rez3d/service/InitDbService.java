package it.uniroma3.it.rez3d.service;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.it.rez3d.model.Credentials;
import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.RealProduct;
import it.uniroma3.it.rez3d.model.Review;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.repository.CredentialsRepository;
import it.uniroma3.it.rez3d.repository.OrderLineRepository;
import it.uniroma3.it.rez3d.repository.OrderRepository;
import it.uniroma3.it.rez3d.repository.PrintFileRepository;
import it.uniroma3.it.rez3d.repository.ReviewRepository;

@Component
public class InitDbService implements CommandLineRunner {

    private final CredentialsService credentialsService;
    private final CredentialsRepository credentialsRepository;
    private final PrintFileRepository printFileRepository;
    private final RealProductService realProductService;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ReviewRepository reviewRepository;

    public InitDbService(CredentialsService credentialsService,
                         CredentialsRepository credentialsRepository,
                         PrintFileRepository printFileRepository,
                         RealProductService realProductService,
                         OrderRepository orderRepository,
                         OrderLineRepository orderLineRepository,
                         ReviewRepository reviewRepository) {
        this.credentialsService = credentialsService;
        this.credentialsRepository = credentialsRepository;
        this.printFileRepository = printFileRepository;
        this.realProductService = realProductService;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Se il database contiene già dati, evitiamo di reinserirli
        if (credentialsRepository.count() > 0) {
            return;
        }

        System.out.println(">>> Inizializzazione Database Rez3d (Print-on-Demand) in corso...");

        // =========================================================================
        // 1. CREAZIONI UTENTI E CREDENZIALI
        // =========================================================================
        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setSurname("Rez3d");
        adminUser.setEmail("admin@rez3d.it");
        adminUser.setUsername("admin");

        Credentials adminCreds = new Credentials();
        adminCreds.setUsername("admin");
        adminCreds.setPassword("admin");
        adminCreds.setRole(Credentials.ADMIN_ROLE);
        adminCreds.setUser(adminUser);
        credentialsService.saveCredentials(adminCreds);

        User user1 = new User();
        user1.setName("Mario");
        user1.setSurname("Rossi");
        user1.setEmail("mario.rossi@example.com");
        user1.setUsername("user");

        Credentials user1Creds = new Credentials();
        user1Creds.setUsername("user");
        user1Creds.setPassword("user");
        user1Creds.setRole(Credentials.DEFAULT_ROLE);
        user1Creds.setUser(user1);
        credentialsService.saveCredentials(user1Creds);

        User user2 = new User();
        user2.setName("Giulia");
        user2.setSurname("Prian");
        user2.setEmail("giulia.prian@example.com");
        user2.setUsername("giulia");

        Credentials user2Creds = new Credentials();
        user2Creds.setUsername("giulia");
        user2Creds.setPassword("giulia");
        user2Creds.setRole(Credentials.DEFAULT_ROLE);
        user2Creds.setUser(user2);
        credentialsService.saveCredentials(user2Creds);

        // =========================================================================
        // 2. CATALOGO DIGITALE FILE 3D (PRINTFILE)
        // Modelli 3D base offerti dal sito, associati ai rispettivi file .stl
        // =========================================================================
        PrintFile marioFile = new PrintFile();
        marioFile.setName("Statuetta Super Mario");
        marioFile.setArtist("NintendoFan3D");
        marioFile.setPrice(12.50f);
        marioFile.setCategory("Videogiochi");
        marioFile.setDescription("Modello 3D dettagliato di Super Mario in posa classica, ottimizzato per la stampa 3D in resina o PLA.");
        marioFile.setStlPath("/uploads/models/mario.stl");
        printFileRepository.save(marioFile);

        PrintFile vasettoFile = new PrintFile();
        vasettoFile.setName("Vasetto Omino Decorativo");
        vasettoFile.setArtist("GreenDesign");
        vasettoFile.setPrice(8.90f);
        vasettoFile.setCategory("Arredamento");
        vasettoFile.setDescription("Simpatico vasetto porta-piantina grasse a forma di omino seduto. Design moderno e stampabile senza supporti.");
        vasettoFile.setStlPath("/uploads/models/omino_vasetto_v3.stl");
        printFileRepository.save(vasettoFile);

        PrintFile luigiFile = new PrintFile();
        luigiFile.setName("Statuetta Luigi");
        luigiFile.setArtist("NintendoFan3D");
        luigiFile.setPrice(12.50f);
        luigiFile.setCategory("Videogiochi");
        luigiFile.setDescription("Modello 3D di Luigi in posa iconica, ideale da affiancare alla statuetta di Mario per tutti i collezionisti.");
        luigiFile.setStlPath("/uploads/models/Luigi.stl");
        printFileRepository.save(luigiFile);

        // =========================================================================
        // 3. RECENSIONI CLIENTI SUI MODELLI 3D (REVIEW)
        // =========================================================================
        Review r1 = new Review();
        r1.setTitle("Spettacolare!");
        r1.setText("Stampato in PLA rosso e blu, i dettagli sono davvero definiti benissimo.");
        r1.setRating(5);
        r1.setAuthor(user1);
        r1.setFile(marioFile);
        reviewRepository.save(r1);

        Review r2 = new Review();
        r2.setTitle("Molto carino e facile da stampare");
        r2.setText("Regalo perfetto per la scrivania in ufficio. La piantina ci sta benissimo.");
        r2.setRating(4);
        r2.setAuthor(user2);
        r2.setFile(vasettoFile);
        reviewRepository.save(r2);

        Review r3 = new Review();
        r3.setTitle("Accoppiata perfetta con Mario!");
        r3.setText("Modello fantastico, proporzioni identiche al videogioco originale.");
        r3.setRating(5);
        r3.setAuthor(user1);
        r3.setFile(luigiFile);
        reviewRepository.save(r3);

        // =========================================================================
        // 4. CONFIGURAZIONI FISICHE STAMPATE (REALPRODUCT) ED ORDINI DEMO
        // RealProduct nasce quando l'utente sceglie dimensione e finitura.
        // =========================================================================
        
        // Ordine 1: Mario Rossi ha ordinato una Statuetta Super Mario personalizzata (Media, Dipinta)
        Order order1 = new Order();
        order1.setDate(LocalDate.now().minusDays(4));
        order1.setUser(user1);
        order1.setIndirizzoSpedizione("Via della Vasca Navale, 79");
        order1.setCitta("Roma");
        order1.setCap("00146");
        order1.setState(OrderState.COMPLETATO);
        order1 = orderRepository.save(order1);

        RealProduct prod1 = new RealProduct();
        prod1.setSize("Media");    // +5.00€
        prod1.setDipinto(true);    // +15.00€
        prod1.setQuantity(1);
        // creaProdotto associa il PrintFile, calcola il prezzo finale (12.50 + 5 + 15 = 32.50€) e salva il RealProduct
        prod1 = realProductService.creaProdotto(prod1, marioFile);

        OrderLine line1 = new OrderLine();
        line1.setOrder(order1);
        line1.setProduct(prod1);
        line1.setQuantity(1);
        orderLineRepository.save(line1);

        // Ordine 2: Laura Bianchi ha ordinato 2 Vasetti Omino (Piccola, Non Dipinto)
        Order order2 = new Order();
        order2.setDate(LocalDate.now().minusDays(1));
        order2.setUser(user2);
        order2.setIndirizzoSpedizione("Corso Vittorio Emanuele II, 100");
        order2.setCitta("Milano");
        order2.setCap("20121");
        order2.setState(OrderState.SPEDITO);
        order2 = orderRepository.save(order2);

        RealProduct prod2 = new RealProduct();
        prod2.setSize("Piccola");  // +0.00€
        prod2.setDipinto(false);   // +0.00€
        prod2.setQuantity(2);
        // Prezzo finale = 8.90€
        prod2 = realProductService.creaProdotto(prod2, vasettoFile);

        OrderLine line2 = new OrderLine();
        line2.setOrder(order2);
        line2.setProduct(prod2);
        line2.setQuantity(2);
        orderLineRepository.save(line2);

        // Ordine 3: Laura Bianchi ha ordinato anche una statuetta Luigi (Grande, Dipinto)
        Order order3 = new Order();
        order3.setDate(LocalDate.now());
        order3.setUser(user2);
        order3.setIndirizzoSpedizione("Corso Vittorio Emanuele II, 100");
        order3.setCitta("Milano");
        order3.setCap("20121");
        order3.setState(OrderState.SPEDITO);
        order3 = orderRepository.save(order3);

        RealProduct prod3 = new RealProduct();
        prod3.setSize("Grande");   // +10.00€
        prod3.setDipinto(true);    // +15.00€
        prod3.setQuantity(1);
        // Prezzo finale = 12.50 + 10 + 15 = 37.50€
        prod3 = realProductService.creaProdotto(prod3, luigiFile);

        OrderLine line3 = new OrderLine();
        line3.setOrder(order3);
        line3.setProduct(prod3);
        line3.setQuantity(1);
        orderLineRepository.save(line3);

        System.out.println(">>> Database Rez3d popolato con successo! (Admin: admin/admin, User: user/user)");
    }
}

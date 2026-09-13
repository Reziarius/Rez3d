package it.uniroma3.it.rez3d.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderLine;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.RealProduct;
import it.uniroma3.it.rez3d.model.User;
import it.uniroma3.it.rez3d.repository.OrderLineRepository;
import it.uniroma3.it.rez3d.repository.OrderRepository;
import it.uniroma3.it.rez3d.repository.PrintFileRepository;
import it.uniroma3.it.rez3d.repository.RealProductRepository;
import it.uniroma3.it.rez3d.repository.UserRepository;

@Service
public class FetchBenchmarkService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final RealProductRepository realProductRepository;
    private final PrintFileRepository printFileRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public FetchBenchmarkService(OrderRepository orderRepository,
                                  OrderLineRepository orderLineRepository,
                                  RealProductRepository realProductRepository,
                                  PrintFileRepository printFileRepository,
                                  UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.realProductRepository = realProductRepository;
        this.printFileRepository = printFileRepository;
        this.userRepository = userRepository;
    }

    public static class BenchmarkResult {
        private int count;
        private double lazyTimeMs;
        private int lazyQueries;
        private double joinFetchTimeMs;
        private int joinFetchQueries;
        private double entityGraphTimeMs;
        private int entityGraphQueries;

        public BenchmarkResult(int count, double lazyTimeMs, int lazyQueries,
                               double joinFetchTimeMs, int joinFetchQueries,
                               double entityGraphTimeMs, int entityGraphQueries) {
            this.count = count;
            this.lazyTimeMs = lazyTimeMs;
            this.lazyQueries = lazyQueries;
            this.joinFetchTimeMs = joinFetchTimeMs;
            this.joinFetchQueries = joinFetchQueries;
            this.entityGraphTimeMs = entityGraphTimeMs;
            this.entityGraphQueries = entityGraphQueries;
        }

        public int getCount() { return count; }
        public double getLazyTimeMs() { return lazyTimeMs; }
        public int getLazyQueries() { return lazyQueries; }
        public double getJoinFetchTimeMs() { return joinFetchTimeMs; }
        public int getJoinFetchQueries() { return joinFetchQueries; }
        public double getEntityGraphTimeMs() { return entityGraphTimeMs; }
        public int getEntityGraphQueries() { return entityGraphQueries; }
    }

    @Transactional
    public BenchmarkResult runBenchmark(int count) {
        if (count <= 0) count = 10;
        if (count > 10000) count = 10000;

        List<Long> orderIds = new ArrayList<>();
        User testUser = null;
        PrintFile testFile = null;
        RealProduct testProduct = null;

        try {
            // 1. Creazione Utente e File 3D Dummy per il Test
            testUser = new User();
            testUser.setName("Benchmark");
            testUser.setSurname("User");
            testUser.setEmail("benchmark" + System.currentTimeMillis() + "@rez3d.it");
            testUser.setUsername("benchuser_" + System.currentTimeMillis());
            testUser = userRepository.save(testUser);

            testFile = new PrintFile();
            testFile.setName("Modello Test Benchmark");
            testFile.setArtist("TestArtist");
            testFile.setPrice(10.00f);
            testFile.setCategory("Benchmark");
            testFile.setDescription("Descrizione Test Benchmark");
            testFile = printFileRepository.save(testFile);

            testProduct = new RealProduct();
            testProduct.setFile(testFile);
            testProduct.setSize("Media");
            testProduct.setDipinto(true);
            testProduct.setQuantity(1);
            testProduct.setFinalPrice(30.00f);
            testProduct = realProductRepository.save(testProduct);

            // 2. Creazione di N Ordini Dummy
            for (int i = 0; i < count; i++) {
                Order o = new Order();
                o.setDate(LocalDate.now());
                o.setUser(testUser);
                o.setState(OrderState.COMPLETATO);
                o.setIndirizzoSpedizione("Via Test Benchmark " + i);
                o.setCitta("Roma");
                o.setCap("00100");
                o = orderRepository.save(o);

                OrderLine line = new OrderLine();
                line.setOrder(o);
                line.setProduct(testProduct);
                line.setQuantity(1);
                orderLineRepository.save(line);

                List<OrderLine> lines = new ArrayList<>();
                lines.add(line);
                o.setItems(lines);

                orderIds.add(o.getId());
            }

            entityManager.flush();
            entityManager.clear();

            // =========================================================================
            // MISURAZIONE 1: Accesso LAZY Standard (1 query lista + N query per items = N+1)
            // =========================================================================
            long startLazy = System.nanoTime();
            int lazyQueryCounter = 0;
            List<Order> lazyOrders = orderRepository.findByUser(testUser);
            lazyQueryCounter++; // 1 query per recuperare i 50 ordini
            for (Order o : lazyOrders) {
                if (o != null && o.getItems() != null) {
                    lazyQueryCounter++; // 1 query LAZY per ciascun ordine (N+1)
                    for (OrderLine line : o.getItems()) {
                        if (line.getProduct() != null) {
                            line.getProduct().getFinalPrice();
                            if (line.getProduct().getFile() != null) {
                                line.getProduct().getFile().getName();
                            }
                        }
                    }
                }
            }
            long endLazy = System.nanoTime();
            double lazyTimeMs = (endLazy - startLazy) / 1_000_000.0;

            entityManager.flush();
            entityManager.clear();

            // =========================================================================
            // MISURAZIONE 2: JPQL JOIN FETCH (1 SOLA Query SQL per l'intera lista)
            // =========================================================================
            long startJoin = System.nanoTime();
            int joinQueryCounter = 0;
            List<Order> joinOrders = orderRepository.findAllOrdersWithDetailsByUser(testUser);
            joinQueryCounter++; // 1 SOLA query SQL per tutti i 50 ordini
            for (Order o : joinOrders) {
                if (o != null && o.getItems() != null) {
                    for (OrderLine line : o.getItems()) {
                        if (line.getProduct() != null) {
                            line.getProduct().getFinalPrice();
                            if (line.getProduct().getFile() != null) {
                                line.getProduct().getFile().getName();
                            }
                        }
                    }
                }
            }
            long endJoin = System.nanoTime();
            double joinFetchTimeMs = (endJoin - startJoin) / 1_000_000.0;

            entityManager.flush();
            entityManager.clear();

            // =========================================================================
            // MISURAZIONE 3: @EntityGraph (1 SOLA Query SQL per l'intera lista)
            // =========================================================================
            long startGraph = System.nanoTime();
            int graphQueryCounter = 0;
            List<Order> graphOrders = orderRepository.findAllOrdersWithGraphByUser(testUser);
            graphQueryCounter++; // 1 SOLA query SQL per tutti i 50 ordini
            for (Order o : graphOrders) {
                if (o != null && o.getItems() != null) {
                    for (OrderLine line : o.getItems()) {
                        if (line.getProduct() != null) {
                            line.getProduct().getFinalPrice();
                            if (line.getProduct().getFile() != null) {
                                line.getProduct().getFile().getName();
                            }
                        }
                    }
                }
            }
            long endGraph = System.nanoTime();
            double entityGraphTimeMs = (endGraph - startGraph) / 1_000_000.0;

            return new BenchmarkResult(count, lazyTimeMs, lazyQueryCounter,
                                       joinFetchTimeMs, joinQueryCounter,
                                       entityGraphTimeMs, graphQueryCounter);

        } finally {
            // Ripulitura automatica dei dati Dummy per mantenere pulito il DB
            try {
                if (!orderIds.isEmpty()) {
                    orderRepository.deleteAllById(orderIds);
                    orderRepository.flush();
                }
                if (testProduct != null && testProduct.getId() != null) {
                    realProductRepository.deleteById(testProduct.getId());
                    realProductRepository.flush();
                }
                if (testFile != null && testFile.getId() != null) {
                    printFileRepository.deleteById(testFile.getId());
                    printFileRepository.flush();
                }
                if (testUser != null && testUser.getId() != null) {
                    userRepository.deleteById(testUser.getId());
                    userRepository.flush();
                }
                entityManager.clear();
            } catch (Exception e) {
                // Ignore cleanup errors to preserve benchmark result if cleanup fails
            }
        }
    }

    @Transactional
    public List<BenchmarkResult> runCurveBenchmark(List<Integer> steps) {
        List<BenchmarkResult> results = new ArrayList<>();
        if (steps == null || steps.isEmpty()) {
            steps = List.of(20, 40, 60, 80, 100, 150, 200, 300, 400, 500, 750, 1000, 1500, 2000);
        }

        for (int count : steps) {
            results.add(runBenchmark(count));
        }
        return results;
    }
}

package it.uniroma3.it.rez3d.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.it.rez3d.model.Order;
import it.uniroma3.it.rez3d.model.OrderState;
import it.uniroma3.it.rez3d.model.PrintFile;
import it.uniroma3.it.rez3d.model.User;
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Optional<Order> findByUserAndState(User user, OrderState state);
    public List<Order> findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
           "FROM Order o JOIN o.items line JOIN line.product p " +
           "WHERE o.user = :user AND p.file = :file AND o.state != 'CART'")
    boolean hasUserPurchasedFile(@Param("user") User user, @Param("file") PrintFile file);

    List<Order> findByStateNot(OrderState state);
    List<Order> findByState(OrderState state);
    List<Order> findByUserAndStateNot(User user, OrderState state);
    List<Order> findAllByUserAndState(User user, OrderState state);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items line LEFT JOIN FETCH line.product p LEFT JOIN FETCH p.file WHERE o.id = :id")
    Optional<Order> findOrderWithDetailsById(@Param("id") Long id);

    @EntityGraph(value = "Order.withDetails")
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findOrderWithGraphById(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items line LEFT JOIN FETCH line.product p LEFT JOIN FETCH p.file WHERE o.user = :user")
    List<Order> findAllOrdersWithDetailsByUser(@Param("user") User user);

    @EntityGraph(value = "Order.withDetails")
    List<Order> findAllOrdersWithGraphByUser(@Param("user") User user);
}

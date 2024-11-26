package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Date;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByCustomerIdAndStatusNotIn(long customerId, List<String> statuses);
    List<Order> findAllByCustomerId(long customer);
    Order findByOrderId(String orderId);
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product " +
            "WHERE o.orderId = :id")
    Optional<Order> findByIdWithSaleLineItems(@Param("id") int id);


    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product " +
            "WHERE o.status = :status")
    List<Order> findByStatusWithSaleLineItems(@Param("status") String status);

    List<Order> findAllByCustomerIdAndStatus(long customerId, String status);

    Optional<Order> findFirstByCustomerIdAndStatusOrderByOrderDateDesc(long customerId, String status);

    @Query("SELECT o.sector, COUNT(o) FROM Order o GROUP BY o.sector")
    List<Object[]> countOrdersBySector();

    @Query("SELECT EXTRACT(HOUR FROM o.orderDate), COUNT(o) FROM Order o GROUP BY EXTRACT(HOUR FROM o.orderDate)")
    List<Object[]> countOrdersByHour();


    @Query("SELECT DATE(o.orderDate), SUM(o.totalPrice) FROM Order o WHERE o.orderDate >= :startDate GROUP BY DATE(o.orderDate) ORDER BY DATE(o.orderDate)")
    List<Object[]> calculateDailyRevenueSince(@Param("startDate") Date startDate);

    @Query("SELECT o.orderDate, SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate GROUP BY o.orderDate")
    List<Object[]> calculateDailyRevenueBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT HOUR(o.orderDate), COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate GROUP BY HOUR(o.orderDate) ORDER BY HOUR(o.orderDate)")
    List<Object[]> countOrdersByHourBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT o.sector, COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate GROUP BY o.sector ORDER BY o.sector")
    List<Object[]> countOrdersBySectorBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

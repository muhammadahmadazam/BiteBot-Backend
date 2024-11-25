package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SalesLineItemRepository extends JpaRepository<SalesLineItem, Long> {
    @Query("SELECT s.product.name, SUM(s.quantity) FROM SalesLineItem s GROUP BY s.product.name ORDER BY SUM(s.quantity) DESC")
    List<Object[]> countTopSoldItems();

    @Query("SELECT s.product.name, SUM(s.quantity) FROM SalesLineItem s WHERE s.order.orderDate BETWEEN :startDate AND :endDate GROUP BY s.product.name ORDER BY SUM(s.quantity) DESC")
    List<Object[]> countTopSoldItemsBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesLineItemRepository extends JpaRepository<SalesLineItem, Long> {
}

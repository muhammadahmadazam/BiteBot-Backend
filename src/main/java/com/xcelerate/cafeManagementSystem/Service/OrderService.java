package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import com.xcelerate.cafeManagementSystem.Repository.OrderRepository;
import com.xcelerate.cafeManagementSystem.Repository.SalesLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final SalesLineItemRepository salesLineItemRepository;

    @Autowired
    OrderService(OrderRepository orderRepository,SalesLineItemRepository salesLineItemRepository) {
        this.orderRepository = orderRepository;
        this.salesLineItemRepository = salesLineItemRepository;
    }

    @Transactional
    public boolean createOrder(Order order) {

        /*  SAVING ORDER IF THE CUSTOMER EXISTS */
        order.setOrderDate(new Date());
        order.setStatus("UNCONFIRMED");
        long total = 0;
        for (SalesLineItem item : order.getOrderItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }
        order.setTotalPrice(total);
        Order o = orderRepository.save(order);

        /*  SAVING ORDER ITEMS  */
//        List<SalesLineItem> saleLineItems = order.getOrderItems();
//        saleLineItems.forEach(lineItem -> {lineItem.setOrder(o);System.out.println("ProductID: " + lineItem.getProduct().getId() + "Quantity: " + lineItem.getQuantity() + "OrderID: " + lineItem.getOrder().getOrderId()); salesLineItemRepository.save(lineItem);});
        return true;
    }


    public boolean isOrderConfirmed() {
        Order  = orderRepository.findByEmail(email);
        if(customer == null) {
            return 0;
        }
        return customer.getVerified() ? 2 : 1;
    }
}

package com.xcelerate.cafeManagementSystem.Service;

import com.google.gson.Gson;
import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import com.xcelerate.cafeManagementSystem.Repository.OrderRepository;
import com.xcelerate.cafeManagementSystem.Repository.SalesLineItemRepository;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final SalesLineItemRepository salesLineItemRepository;

    @Autowired
    private OpenRouteService openRouteService;


    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

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

    public List<Order> getConfirmedOrdersByCustomerId(long customerId) {
        return orderRepository.findAllByCustomerIdOrStatusNot(customerId, "UNCONFIRMED");
    }

    public String getEstimatedTime(String dest_lat, String dest_lon) {
        return openRouteService.getEstimatedTime(dest_lat, dest_lon);
    }


    public Map<String, Long> getOrderCountBySector(){
        List<Object[]> sectorCount = orderRepository.countOrdersBySector();
        Map<String, Long> sectorCountMap = new HashMap<>();
        for (Object[] obj : sectorCount) {
            if(obj[0] instanceof String && obj[1] instanceof Long) {
                sectorCountMap.put((String) obj[0], (Long) obj[1]);
            }
        }
        return sectorCountMap;
    }

    public Map<String, Long> getTopSoldItems() {
        List<Object[]> results = salesLineItemRepository.countTopSoldItems();
        Map<String, Long> topSoldItems = new HashMap<>();
        for (Object[] result : results) {
            topSoldItems.put((String) result[0], (Long) result[1]);
        }
        return topSoldItems;
    }

    public Map<Integer, Long> getTopBusinessHours() {
        List<Object[]> results = orderRepository.countOrdersByHour();
        Map<Integer, Long> orderCountByHour = new HashMap<>();
        for (Object[] result : results) {
            orderCountByHour.put(((Number) result[0]).intValue(), (Long) result[1]);
        }
        return orderCountByHour;
    }

    public Map<String, Long> getDailyRevenueForLast30Days() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = calendar.getTime();
        logger.info("Fetching daily revenue since: " + startDate);
        List<Object[]> results = null;
        try {
            results = orderRepository.calculateDailyRevenueSince(startDate);
        } catch (Exception e) {
            logger.severe("Error executing query: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching daily revenue", e);
        }
        Map<String, Long> dailyRevenue = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Object[] result : results) {
            if (result[0] instanceof java.util.Date && result[1] instanceof Number) {
                String dateString = dateFormat.format((java.util.Date) result[0]);
                dailyRevenue.put(dateString, ((Number) result[1]).longValue());

            }
        }

        return dailyRevenue;
    }

    public Map<String, Long> getDailyRevenueBetweenDates(Date startDate, Date endDate) {
        logger.info("Fetching daily revenue between: " + startDate + " and " + endDate);
        List<Object[]> results = null;
        try {
            results = orderRepository.calculateDailyRevenueBetweenDates(startDate, endDate);
        } catch (Exception e) {
            logger.severe("Error executing query: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching daily revenue", e);
        }
        Map<String, Long> dailyRevenue = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Object[] result : results) {
            if (result[0] instanceof java.util.Date && result[1] instanceof Number) {
                String dateString = dateFormat.format((java.util.Date) result[0]);
                dailyRevenue.put(dateString, ((Number) result[1]).longValue());
            }
        }
        return dailyRevenue;
    }

    public Map<Integer, Long> getTopBusinessHoursBetweenDates(Date startDate, Date endDate) {
        List<Object[]> results = orderRepository.countOrdersByHourBetweenDates(startDate, endDate);
        Map<Integer, Long> orderCountByHour = new HashMap<>();
        for (Object[] result : results) {
            orderCountByHour.put(((Number) result[0]).intValue(), (Long) result[1]);
        }
        return orderCountByHour;
    }

    public Map<String, Long> getTopSoldItemsBetweenDates(Date startDate, Date endDate) {
        List<Object[]> results = salesLineItemRepository.countTopSoldItemsBetweenDates(startDate, endDate);
        Map<String, Long> topSoldItems = new HashMap<>();
        for (Object[] result : results) {
            topSoldItems.put((String) result[0], (Long) result[1]);
        }
        return topSoldItems;
    }

    public Map<String, Long> getOrderCountBySectorBetweenDates(Date startDate, Date endDate) {
        List<Object[]> results = orderRepository.countOrdersBySectorBetweenDates(startDate, endDate);
        Map<String, Long> orderCountBySector = new HashMap<>();
        for (Object[] result : results) {
            if(result[0] instanceof String && result[1] instanceof Long){
                orderCountBySector.put((String) result[0], (Long) result[1]);
            }
        }
        return orderCountBySector;
    }

}

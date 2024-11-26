package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/insights")
@CrossOrigin(origins = "${frontendURL}")
public class CafeInsightsController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders-by-sector")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getOrdersBySector(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Map<String, Long> ordersBySector;
        if (startDate != null && endDate != null) {
            ordersBySector = orderService.getOrderCountBySectorBetweenDates(startDate, endDate);
        } else {
            ordersBySector = orderService.getOrderCountBySector();
        }
        ApiResponseDTO<Map<String, Long>> response = new ApiResponseDTO<>("Orders by sector", ordersBySector);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-business-hours")
    public ResponseEntity<ApiResponseDTO<Map<Integer, Long>>> getTopBusinessHours(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Map<Integer, Long> topBusinessHours;
        if (startDate != null && endDate != null) {
            topBusinessHours = orderService.getTopBusinessHoursBetweenDates(startDate, endDate);
        } else {
            topBusinessHours = orderService.getTopBusinessHours();
        }
        ApiResponseDTO<Map<Integer, Long>> response = new ApiResponseDTO<>("Top business hours", topBusinessHours);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-sold-items")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getTopSoldItems(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Map<String, Long> topSoldItems;
        if (startDate != null && endDate != null) {
            topSoldItems = orderService.getTopSoldItemsBetweenDates(startDate, endDate);
        } else {
            topSoldItems = orderService.getTopSoldItems();
        }
        ApiResponseDTO<Map<String, Long>> response = new ApiResponseDTO<>("Top sold items", topSoldItems);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-revenue")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getRevenue(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Map<String, Long> dailyRevenue;
        if (startDate != null && endDate != null) {
            dailyRevenue = orderService.getDailyRevenueBetweenDates(startDate, endDate);
        } else {
            dailyRevenue = orderService.getDailyRevenueForLast30Days();
        }
        ApiResponseDTO<Map<String, Long>> response = new ApiResponseDTO<>("Daily revenue data", dailyRevenue);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid date format or value: " + ex.getValue();
        ApiResponseDTO<String> response = new ApiResponseDTO<>(errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
package com.javachallenge.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javachallenge.application.entity.Order;
import com.javachallenge.application.service.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/return-payment-url")
    public String returnPaymentUrl(Order order) {

        Order firstTest = new Order();
        firstTest.productsByPerson = new double[][]{
            new double[]{ 40d, 2d }, 
            new double[]{ 8d }
        };
        firstTest.flatDiscounts = 20d;
        firstTest.flatTaxes = 8d;

        return orderService.returnPaymentUrl(firstTest);
        //return orderService.returnPaymentUrl(order);
    }
}

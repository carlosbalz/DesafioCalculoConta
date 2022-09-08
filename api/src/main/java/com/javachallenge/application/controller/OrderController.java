package com.javachallenge.application.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.Result;
import com.javachallenge.application.service.OrderService;
import com.javachallenge.application.validator.OrderValidator;


@CrossOrigin
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/return-payment-links", consumes = "application/json")
    public ResponseEntity<Object> returnPaymentLinks(String json) {
        try {
            Order order = new Gson().fromJson(json, Order.class);

            ArrayList<String> validationMessages = OrderValidator.getValidationMessages(order);
            if (validationMessages.size() > 0) {
                Result<ArrayList<String>> result = new Result<ArrayList<String>>(validationMessages, false);
                return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
            }

            String[] paymentLinks = orderService.getPaymentLinks(order);
            Result<String[]> result = new Result<String[]>(paymentLinks, true);
            return new ResponseEntity<Object>(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

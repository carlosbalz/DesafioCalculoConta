package com.javachallenge.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.Result;
import com.javachallenge.application.service.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @RequestMapping("/return-payment-links")
    public String returnPaymentLinks(String orderJson) throws Exception {
        orderJson = """
                { 
                    "flatTaxes":8,
                    "flatDiscounts":20,
                    "percentageTaxes":0,
                    "percentageDiscounts":0,
                    "paymentMethod": "paypal",                    
                    "subOrders":[
                        {
                            "payerName": "Bruno",
                            "paymentCurrency":"BRL",
                            "products":[
                                {
                                    "price":40,
                                    "name":"hamburguer"
                                },
                                {
                                    "price":2,
                                    "name":"sorvete"
                                }
                            ]
                        },
                        {
                            "payerName":"Fernanda", 
                            "paymentCurrency":"BRL",                       
                            "products":[
                                {
                                    "price":8,
                                    "name":"sanduba"
                                }
                            ]
                        }
                    ]
                }""";        

            Gson gson = new Gson();
            Order order = gson.fromJson(orderJson, Order.class);            
            String[] paymentLinks = orderService.getPaymentLinks(order);
            Result<String[]> result = new Result<String[]>(paymentLinks, true);
            return gson.toJson(result);     
    }
}

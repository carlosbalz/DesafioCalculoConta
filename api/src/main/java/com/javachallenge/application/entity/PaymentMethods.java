package com.javachallenge.application.entity;

import com.javachallenge.application.Interface.Payable;
import com.javachallenge.application.service.PaypalService;

public class PaymentMethods {

    public static Payable getPaymentMethod(String paymentMethod) {
        switch (paymentMethod) {
            case "paypal":
                return new PaypalService();
            default:
                return null;
        }
    }

    private PaymentMethods() {
        
    }
}

package com.javachallenge.application.entity;

public class PaymentMethods {

    public static Payable getPaymentMethod(String paymentMethod) {
        switch (paymentMethod) {
            case "paypal":
                return new Paypal();
            default:
                return null;
        }
    }
}

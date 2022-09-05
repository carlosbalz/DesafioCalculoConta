package com.javachallenge.application.entity;

public class SubOrder {
    private String payerName;
    private Product[] products;
    private String paymentCurrency; 
    private double totalPrice;
    private double finalPrice;

    public String getPayerName() {
        return payerName;
    }
    
    public Product[] getProducts() {
        return products;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public double getTotalPrice() {
        for(int i = 0; i < products.length; i++) {
            totalPrice += products[i].getPrice();
        }
        return totalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double price) {        
        finalPrice = price;
    }
}

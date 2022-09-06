package com.javachallenge.application.entity;

public class SubOrder {
    private String payerName;
    private Product[] products;
    private String paymentCurrency; 
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

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double price) {        
        finalPrice = price;
    }    

    public double getTotalPrice() {
        double total = 0d;
        for(int i = 0; i < products.length; i++) {
            total += products[i].getPrice();
        }
        return total;
    }
}

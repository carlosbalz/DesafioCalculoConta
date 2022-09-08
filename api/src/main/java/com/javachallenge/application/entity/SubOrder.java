package com.javachallenge.application.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SubOrder {
    private String payerName;    
    private double[] products;    
    private String paymentCurrency; 
    //set after all the taxes and discounts are cal    
    private double finalPrice;

    public String getPayerName() {
        return payerName;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }    

    public double getFinalPrice() {
        return finalPrice;
    }

    public double[] getProducts() {
        return products;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public void setProducts(double[] products) {
        this.products = products;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public void setFinalPrice(double price) {        
        finalPrice = price;
    }    

    public double getTotalPrice() {
        double total = 0d;
        for(int i = 0; i < products.length; i++) {
            total += products[i];
        }
        return total;
    }    
}

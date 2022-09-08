package com.javachallenge.application.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Order {    
    private SubOrder[] subOrders;    
    private double flatTaxes;    
    private double flatDiscounts;
    private double percentageTaxes;
    private double percentageDiscounts;
    private String paymentMethod;            

    public double getFlatTax() {
        return flatTaxes;
    }

    public double getFlatDiscount() {
        return flatDiscounts;
    }

    public double getPercentageTax() {
        return percentageTaxes;
    }

    public double getPercentageDiscount() {
        return percentageDiscounts;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public SubOrder[] getSubOrders() {
        return subOrders;
    }

    public void setFlatTaxes(double flatTaxes) {
        this.flatTaxes = flatTaxes;
    }

    public void setPercentageTax(double percentageTax) {
        this.percentageTaxes = percentageTax;
    }
    
    public void setFlatDiscounts(double flatDiscounts) {
        this.flatDiscounts = flatDiscounts;
    }

    public void setPercentageDiscounts(double percentageDiscount) {
        this.percentageDiscounts = percentageDiscount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }    

    public void setSubOrders(SubOrder[] subOrders) {
        this.subOrders = subOrders;
    }        
}

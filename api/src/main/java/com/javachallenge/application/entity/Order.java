package com.javachallenge.application.entity;

public class Order {
    public double[][] productsByPerson;
    public double flatTaxes;
    public double flatDiscounts;
    public double percentageTaxes;
    public double percentageDiscounts;

    public double[][] getProductsByPerson() {
        return productsByPerson;
    }

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
}

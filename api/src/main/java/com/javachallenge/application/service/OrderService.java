package com.javachallenge.application.service;

import org.springframework.stereotype.Service;

import com.javachallenge.application.Interface.Payable;
import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.PaymentMethods;
import com.javachallenge.application.entity.SubOrder;

@Service
public class OrderService {

    public String[] getPaymentLinks(Order order) throws Exception {
        String paymentMethodName = order.getPaymentMethod();
        Payable paymentMethod = PaymentMethods.getPaymentMethod(paymentMethodName);
        String token = paymentMethod.getToken();
        SubOrder[] subOrders = getCalculatedSubOrders(order);
        return createPaymentLinks(paymentMethod, token, subOrders);
    }

    private String[] createPaymentLinks(Payable paymentMethod, String token, SubOrder[] subOrders) throws Exception {
        String[] links = new String[subOrders.length];
        for (int i = 0; i < subOrders.length; i++) {
            links[i] = paymentMethod.getPaymentLink(token, subOrders[i]);
        }
        return links;
    }

    private SubOrder[] getCalculatedSubOrders(Order order) {
        SubOrder[] subOrders = order.getSubOrders();
        double orderTotal = getOrderTotalPrice(subOrders);

        for (int i = 0; i < subOrders.length; i++) {
            double subOrderTotal = subOrders[i].getTotalPrice();
            double discountsAndTaxes = getDiscountsAndTaxes(order, subOrderTotal, orderTotal);
            subOrders[i].setFinalPrice(subOrderTotal + discountsAndTaxes);
        }
        return subOrders;
    }

    private double getOrderTotalPrice(SubOrder[] subOrders) {
        double total = 0d;
        for (int i = 0; i < subOrders.length; i++) {
            total += subOrders[i].getTotalPrice();
        }
        return total;
    }

    private double getDiscountsAndTaxes(Order order, double subOrderTotal, double orderTotal) {
        double discountsAndTaxes = 0d;
        double percentageFromOrderTotal = subOrderTotal / orderTotal;

        discountsAndTaxes -= getFlatDiscount(order, percentageFromOrderTotal);

        discountsAndTaxes -= getDiscountFromPercentage(order, subOrderTotal);

        discountsAndTaxes += getFlatTaxes(order, percentageFromOrderTotal);

        discountsAndTaxes += getTaxesFromPercentage(order, subOrderTotal);

        return discountsAndTaxes;
    }

    private double getFlatDiscount(Order order, double percentageFromOrderTotal) {
        double flatDiscount = order.getFlatDiscount();
        if (Double.compare(flatDiscount, 0) > 0) {
            return flatDiscount * percentageFromOrderTotal;
        }
        return 0;
    }

    private double getDiscountFromPercentage(Order order, double totalPerSubOrder) {
        double percentageDiscount = order.getPercentageDiscount();
        if (Double.compare(percentageDiscount, 0) > 0) {
            return totalPerSubOrder * percentageDiscount;
        }
        return 0;
    }

    private double getFlatTaxes(Order order, double percentageFromOrderTotal) {
        double flatTax = order.getFlatTax();
        if (Double.compare(flatTax, 0) > 0) {
            return flatTax * percentageFromOrderTotal;
        }
        return 0;
    }

    private double getTaxesFromPercentage(Order order, double totalPerSubOrder) {
        double percentageTax = order.getPercentageTax();
        if (Double.compare(percentageTax, 0) > 0) {
            return totalPerSubOrder * percentageTax;
        }
        return 0;
    }
}
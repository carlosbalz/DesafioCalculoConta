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

    protected String[] createPaymentLinks(Payable paymentMethod, String token, SubOrder[] subOrders) throws Exception {
        String[] links = new String[subOrders.length];
        for (int i = 0; i < subOrders.length; i++) {
            String link = paymentMethod.getPaymentLink(token, subOrders[i]);
            links[i] = new String(link);
        }
        return links;
    }

    protected SubOrder[] getCalculatedSubOrders(Order order) {
        SubOrder[] subOrders = order.getSubOrders();
        double orderTotal = getOrderTotalPrice(subOrders);

        for (int i = 0; i < subOrders.length; i++) {
            double subOrderTotal = subOrders[i].getTotalPrice();
            double discountsAndTaxes = getDiscountsAndTaxes(order, subOrderTotal, orderTotal);
            subOrders[i].setFinalPrice(subOrderTotal + discountsAndTaxes);
        }
        return subOrders;
    }

    protected double getOrderTotalPrice(SubOrder[] subOrders) {
        double total = 0d;
        for (int i = 0; i < subOrders.length; i++) {
            total += subOrders[i].getTotalPrice();
        }
        return total;
    }

    protected double getDiscountsAndTaxes(Order order, double subOrderTotal, double orderTotal) {
        double discountsAndTaxes = 0d;
        double percentageFromOrderTotal = subOrderTotal / orderTotal;

        discountsAndTaxes -= getFlatDiscount(order, percentageFromOrderTotal);

        discountsAndTaxes -= getDiscountFromPercentage(order, subOrderTotal);

        discountsAndTaxes += getFlatTaxes(order, percentageFromOrderTotal);

        discountsAndTaxes += getTaxesFromPercentage(order, subOrderTotal);

        return discountsAndTaxes;
    }

    protected double getFlatDiscount(Order order, double percentageFromOrderTotal) {
        double flatDiscount = order.getFlatDiscount();
        if (Double.compare(flatDiscount, 0) > 0) {
            return flatDiscount * percentageFromOrderTotal;
        }
        return 0;
    }

    protected double getDiscountFromPercentage(Order order, double subOrderTotal) {
        double percentageDiscount = order.getPercentageDiscount();
        if (Double.compare(percentageDiscount, 0) > 0) {
            return subOrderTotal * (percentageDiscount / 100);
        }
        return 0;
    }

    protected double getFlatTaxes(Order order, double percentageFromOrderTotal) {
        double flatTax = order.getFlatTax();
        if (Double.compare(flatTax, 0) > 0) {
            return flatTax * percentageFromOrderTotal;
        }
        return 0;
    }

    protected double getTaxesFromPercentage(Order order, double subOrderTotal) {
        double percentageTax = order.getPercentageTax();
        if (Double.compare(percentageTax, 0) > 0) {
            return subOrderTotal * (percentageTax / 100);
        }
        return 0;
    }
}
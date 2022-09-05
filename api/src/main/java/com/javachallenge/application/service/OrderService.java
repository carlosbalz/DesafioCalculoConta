package com.javachallenge.application.service;

import org.springframework.stereotype.Service;
import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.Payable;
import com.javachallenge.application.entity.PaymentMethods;
import com.javachallenge.application.entity.SubOrder;

@Service
public class OrderService {

    public String[] returnPaymentLinks(Order order) {
        try {
            Payable paymentMethod = PaymentMethods.getPaymentMethod(order.getPaymentMethod());
            String token = paymentMethod.getToken();            
                       
            calculateTaxesAndDiscountsPerSubOrder(order);

            SubOrder[] subOrders = order.getSubOrders(); 
            String[] paymentLinks = new String[subOrders.length];
            for(int i = 0; i < subOrders.length; i++) {
                paymentLinks[i] = paymentMethod.getPaymentLink(token, subOrders[i]);            
            }                
            return paymentLinks;

        } catch (Exception e) {
            return null;
        }
    }

    private SubOrder[] calculateTaxesAndDiscountsPerSubOrder(Order order) {
        SubOrder[] subOrders = order.getSubOrders();
        double[] totalPerSubOrder = new double[subOrders.length];
        double orderTotal = 0d;

        // calculates the total price of each suborder without discounts and taxes
        for (int i = 0; i < subOrders.length; i++) {
            double subOrderTotal = subOrders[i].getTotalPrice();
            orderTotal += subOrderTotal;
            totalPerSubOrder[i] = subOrderTotal;
        }

        // calculates the price from discounts and taxes for each suborder
        for (int i = 0; i < totalPerSubOrder.length; i++) {
            double discountsAndTaxes = 0d;
            double percentageFromOrderTotal = totalPerSubOrder[i] / orderTotal;            

            double flatDiscount = order.getFlatDiscount();
            if (flatDiscount > 0) {
                discountsAndTaxes -= flatDiscount * percentageFromOrderTotal;
            }

            double percentageDiscount = order.getPercentageDiscount();
            if (percentageDiscount > 0) {
                discountsAndTaxes -= totalPerSubOrder[i] * percentageDiscount;
            }

            double flatTax = order.getFlatTax();
            if (flatTax > 0) {
                discountsAndTaxes += flatTax * percentageFromOrderTotal;
            }

            double percentageTax = order.getPercentageTax();
            if (percentageTax > 0) {
                discountsAndTaxes += totalPerSubOrder[i] * percentageTax;
            }

            order.getSubOrders()[i].setFinalPrice(totalPerSubOrder[i] + discountsAndTaxes);
        }
        return subOrders;
    }    
}
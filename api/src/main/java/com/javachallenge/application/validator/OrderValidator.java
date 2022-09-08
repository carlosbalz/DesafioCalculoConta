package com.javachallenge.application.validator;

import java.util.ArrayList;

import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.SubOrder;

public class OrderValidator {

    public static ArrayList<String> getValidationMessages(Order order) {
        ArrayList<String> messages = new ArrayList<String>();

        if(order.getFlatTax() < 0) {
            messages.add("Flat tax value cannot be less than 0");
        } 

        if(order.getFlatDiscount() < 0) {
            messages.add("Flat discount value cannot be less than 0");
        }

        if(order.getPercentageTax() < 0) {
            messages.add("Percentage tax value cannot be less than 0");
        }

        if(order.getPercentageDiscount() < 0) {
            messages.add("Percentage discount value cannot be less than 0");
        }

        if(order.getPaymentMethod().trim() == "") {
            messages.add("Payment method cannot be empty");
        }

        if(order.getSubOrders().length <= 0) {
            messages.add("Sub-orders list cannot be empty");
        }

        for(SubOrder subOrder : order.getSubOrders()) {
            ArrayList<String> subOrderValidationMessages = SubOrderValidator.getValidationMessages(subOrder);

            if(subOrderValidationMessages.size() > 0) {
                messages.addAll(subOrderValidationMessages);
            }
        }
        return messages;
    }

}

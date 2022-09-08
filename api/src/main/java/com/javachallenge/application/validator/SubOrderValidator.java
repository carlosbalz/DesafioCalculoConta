package com.javachallenge.application.validator;

import java.util.ArrayList;

import com.javachallenge.application.entity.SubOrder;

public class SubOrderValidator {

    public static ArrayList<String> getValidationMessages(SubOrder subOrder) {
        ArrayList<String> messages = new ArrayList<String>();
        
        if(subOrder.getPayerName().trim() == "") {
            messages.add("Payer name cannot be empty");
        }

        if(subOrder.getPaymentCurrency().trim() == "") {
            messages.add("Payment currency cannot be empty");
        }

        if(subOrder.getProducts().length <= 0) {
            messages.add("Products prices's list cannot be empty");
        }

        if(subOrder.getTotalPrice() <= 0) {
            messages.add("Product's list must have at least one value bigger than 0");
        }

        return messages;
    }

}

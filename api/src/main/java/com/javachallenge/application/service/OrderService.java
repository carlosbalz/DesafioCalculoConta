package com.javachallenge.application.service;

import java.text.DecimalFormat;

import org.springframework.stereotype.Service;

import com.javachallenge.application.entity.Order;

@Service
public class OrderService {

    public String returnPaymentUrl(Order order) {

        double[] values = calculateValueByPerson(order);
        return values.toString();
    }

    private double[] calculateValueByPerson(Order order) {
        double[][] productsByPerson = order.getProductsByPerson();        
        double[] finalPrices, totalValueByPerson;
        finalPrices = totalValueByPerson = new double[productsByPerson.length];                
        double orderTotalPrice = 0d;      

        //calcula o valor total de cada pedido sem descontos e taxas
        for(int i = 0; i < productsByPerson.length; i++) {
            
            double personTotalValue = 0d;

            for(int x = 0; x < productsByPerson[i].length; x++) {
                personTotalValue += productsByPerson[i][x];
            }
            orderTotalPrice += personTotalValue;
            totalValueByPerson[i] = personTotalValue;
        }   
        
        //calcula o valor dos descontos e taxas para cada pedido
        for(int i = 0; i < totalValueByPerson.length; i++) {
            double percentageFromTotal = totalValueByPerson[i] / orderTotalPrice;
            double discountsAndTaxes = 0d;

            double flatDiscount = order.getFlatDiscount();
            if(flatDiscount > 0) {                
                discountsAndTaxes -= flatDiscount * percentageFromTotal;
            }                        

            double percentageDiscount = order.getPercentageDiscount();
            if(percentageDiscount > 0) {
                discountsAndTaxes -= totalValueByPerson[i] * percentageDiscount;
            }

            double flatTax = order.getFlatTax();
            if(flatTax > 0) {
                discountsAndTaxes += flatTax * percentageFromTotal;
            }

            double percentageTax = order.getPercentageTax();
            if(percentageTax > 0) {
                discountsAndTaxes += totalValueByPerson[i] * percentageTax;
            }

            finalPrices[i] = totalValueByPerson[i] + discountsAndTaxes;
        }        
        return finalPrices;     
    }    
}
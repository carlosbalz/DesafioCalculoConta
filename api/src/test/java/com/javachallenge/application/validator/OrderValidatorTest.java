package com.javachallenge.application.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.SubOrder;

public class OrderValidatorTest {

    private Order order;

    @Test
    public void shouldValidationPass() {
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(0, messages.size());
    }

    @Test
    public void shouldValidationFailWhenFlatTaxesAreLessThanZero() {
        order.setFlatTaxes(-1);
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(1, messages.size());
    }

    @Test
    public void shouldValidationFailWhenFlatDiscountsAreLessThanZero() {
        order.setFlatDiscounts(-1);
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(1, messages.size());
    }

    @Test
    public void shouldValidationFailWhenPercentageTaxesAreLessThanZero() {
        order.setPercentageTax(-1);
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(1, messages.size());
    }

    @Test
    public void shouldValidationFailWhenPercentageDiscountsAreLessThanZero() {
        order.setPercentageDiscounts(-1);
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(1, messages.size());
    }

    @Test
    public void shouldValidationFailWhenSubOrdersListIsEmpty() {
        order.setSubOrders(new SubOrder[0]);
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(1, messages.size());
    }

    @Test
    public void shouldValidationFailWhenPaymentMethodIsEmpty() {
        order.setPaymentMethod("");
        ArrayList<String> messages =  OrderValidator.getValidationMessages(order);
        assertEquals(1, messages.size());
    }

    @BeforeEach
    protected void setUp() {
        SubOrder subOrder = new SubOrder("Tester", new double[] { 100d }, "BRL", 0d);
        SubOrder[] subOrders = new SubOrder[] { subOrder };
        order = new Order(subOrders, 0d, 0d, 0d, 0d, "paypal");                    
    }
}

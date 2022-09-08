package com.javachallenge.application.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.javachallenge.application.entity.SubOrder;

public class SubOrderValidatorTest {

    private SubOrder subOrder;

    @Test
    public void shouldValidationPass() {
        ArrayList<String> messages = SubOrderValidator.getValidationMessages(subOrder);
        assertEquals(0, messages.size());
    }

    @Test
    public void shouldValidationFailWhenPayerNameIsEmpty() {
        subOrder.setPayerName("");
        ArrayList<String> messages = SubOrderValidator.getValidationMessages(subOrder);
        assertEquals(1, messages.size());
    }

    @Test
    public void shouldValidationFailWhenProductsListIsEmpty() {
        //if the list is empty the total price also should be 0
        subOrder.setProducts(new double[0]);
        ArrayList<String> messages = SubOrderValidator.getValidationMessages(subOrder);
        assertEquals(2, messages.size());
        assertEquals(0d, subOrder.getTotalPrice());
    }

    @Test
    public void shouldValidationFailWhenPaymentCurrencyIsEmpty() {
        subOrder.setPaymentCurrency("");
        ArrayList<String> messages = SubOrderValidator.getValidationMessages(subOrder);
        assertEquals(1, messages.size());
    }

    @BeforeEach
    protected void setUp() {
        subOrder = new SubOrder("Tester", new double[] { 100d }, "BRL", 0d);        
    }
}

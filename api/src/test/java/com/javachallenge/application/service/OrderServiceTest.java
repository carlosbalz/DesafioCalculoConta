package com.javachallenge.application.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.javachallenge.application.Interface.Payable;
import com.javachallenge.application.entity.Order;
import com.javachallenge.application.entity.PaymentMethods;
import com.javachallenge.application.entity.SubOrder;

public class OrderServiceTest {

    @Autowired
    OrderService orderService = new OrderService();

    @Autowired
    Payable payable;

    private Order order;

    private SubOrder[] subOrders;

    private Payable paymentMethod = PaymentMethods.getPaymentMethod("paypal");

    private double orderTotal;

    @Test
    public void shouldThrowAnExceptionWhenTokenIsEmpty() throws Exception {
        OrderService mockedOrderService = mock(OrderService.class);

        when(mockedOrderService.createPaymentLinks(any(), eq(""), any()))
        .thenThrow(new Exception("invalid token"));

        Exception exception = assertThrows(Exception.class, () -> mockedOrderService.createPaymentLinks(paymentMethod, "", subOrders));
        assertEquals("invalid token", exception.getMessage());
    }

    @Test
    public void shouldReturnAnArrayOfStringsWhenTokenIsValid() throws Exception {
        OrderService mockedOrderService = mock(OrderService.class);
        
        when(mockedOrderService.createPaymentLinks(any(), eq("test"), any()))
        .thenReturn(new String[] { "www.testlink.com" });

        String[] links = mockedOrderService.createPaymentLinks(paymentMethod, "test", subOrders);
        assertEquals("www.testlink.com", links[0]);
    }

    @Test
    public void shouldReturnDifferentSubOrdersWhenThereAreDifferentsTaxesAndDiscounts() {        
        double noTaxesAndDiscountsPrice = orderService.getDiscountsAndTaxes(order, subOrders[0].getTotalPrice(), orderTotal);
        order.setFlatDiscounts(10);
        order.setFlatTaxes(5);
        order.setPercentageDiscounts(10);
        order.setPercentageTax(5);
        SubOrder[] newSubOrders = orderService.getCalculatedSubOrders(order);
        double taxesAndDiscountsPrice = orderService.getDiscountsAndTaxes(order, newSubOrders[0].getTotalPrice(), orderTotal);
        assertNotEquals(taxesAndDiscountsPrice, noTaxesAndDiscountsPrice);
        assertNotEquals(0d, newSubOrders[0].getFinalPrice());
        assertEquals(subOrders.length, newSubOrders.length);
    }

    @Test
    public void shouldReturnZeroWhenSubOrdersListIsEmpty() {
        order.setSubOrders(new SubOrder[0]);
        SubOrder[] subOrders = order.getSubOrders();
        double orderTotal = orderService.getOrderTotalPrice(subOrders);
        assertEquals(0d, orderTotal);
    }

    @Test
    public void shouldReturnZeroWhenThereAreNoTaxesAndDiscounts() {
        double subOrderTotal = subOrders[0].getTotalPrice();
        double discountsAndTaxes = orderService.getDiscountsAndTaxes(order, subOrderTotal, orderTotal);
        assertEquals(0d, discountsAndTaxes);
    }

    @Test
    public void shouldReturnDifferentOfZeroWhenThereAreDifferentTaxesAndDiscounts() {
        order.setFlatDiscounts(10);
        order.setFlatTaxes(5);
        order.setPercentageDiscounts(10);
        order.setPercentageTax(5);

        double subOrderTotal = subOrders[0].getTotalPrice();
        double discountsAndTaxes = orderService.getDiscountsAndTaxes(order, subOrderTotal, orderTotal);
        assertNotEquals(0d, discountsAndTaxes);   
        assertEquals(-10d, discountsAndTaxes);     
    }

    @Test
    public void shouldReturnZeroWhenFlatTaxIsZero() {        
        order.setFlatTaxes(0);
        double subOrderTotal = subOrders[0].getTotalPrice();
        double percentageFromOrderTotal = subOrderTotal / orderService.getOrderTotalPrice(subOrders);
        double taxes = orderService.getFlatTaxes(order, percentageFromOrderTotal);
        assertEquals(0, taxes);
    }

    @Test
    public void shouldReturnDifferentOfZeroWhenFlatTaxIsDifferentOfZero() {        
        order.setFlatTaxes(25);
        double subOrderTotal = subOrders[0].getTotalPrice();
        double percentageFromOrderTotal = subOrderTotal / orderTotal;
        double taxes = orderService.getFlatTaxes(order, percentageFromOrderTotal);
        assertEquals(25, taxes);
        assertEquals(125, orderTotal + taxes);
    }

    @Test
    public void shouldReturnZeroWhenPercentageTaxIsZero() {        
        double subOrderTotal = subOrders[0].getTotalPrice();
        double taxes = orderService.getTaxesFromPercentage(order, subOrderTotal);
        assertEquals(0d, taxes);
    }

    @Test
    public void shouldReturnDifferentOfZeroWhenPercentageTaxIsDifferentOfZero() {        
        double subOrderTotal = subOrders[0].getTotalPrice();
        order.setPercentageTax(50);        
        double taxes = orderService.getTaxesFromPercentage(order, subOrderTotal);
        assertEquals(50d, taxes);
        assertEquals(150, subOrderTotal + taxes);
    }

    @BeforeEach
    protected void setUp() {
        SubOrder subOrder = new SubOrder("Tester", new double[] { 100d }, "BRL", 0d);
        SubOrder[] subOrders = new SubOrder[] { subOrder };
        order = new Order(subOrders, 0d, 0d, 0d, 0d, "paypal");                            
        this.subOrders = subOrders;
        this.orderTotal = orderService.getOrderTotalPrice(subOrders);
    }
}

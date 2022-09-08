package com.javachallenge.application.service;

import org.junit.jupiter.api.Test;

import com.javachallenge.application.entity.SubOrder;
import com.paypal.base.rest.PayPalRESTException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaypalServiceTest {

    private SubOrder subOrder = new SubOrder(
            "Tester",
            new double[] { 100d },
            "BRL",
            0d);

    private PaypalService mockedPaypalService = mock(PaypalService.class);

    @Test
    public void shouldReturnStringWhenTokenAndSubOrderAreValid() throws PayPalRESTException {
        when(mockedPaypalService.getPaymentLink(eq("test"), eq(subOrder)))
        .thenReturn("www.testlink.com");

        String paymentLink = mockedPaypalService.getPaymentLink("test", subOrder);
        assertEquals("www.testlink.com", paymentLink);
    }

    @Test
    public void shouldThrowExceptionWhenTokenIsEmpty() throws PayPalRESTException {
        when(mockedPaypalService.createPaypalPayment(eq(""), any()))
        .thenThrow(new PayPalRESTException("invalid token"));        

        Exception exception = assertThrows(PayPalRESTException.class, () -> mockedPaypalService.createPaypalPayment("", subOrder));
        assertEquals("invalid token", exception.getMessage());
    }
}

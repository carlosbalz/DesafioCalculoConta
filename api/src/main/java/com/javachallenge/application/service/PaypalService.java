package com.javachallenge.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.javachallenge.application.Interface.Payable;
import com.javachallenge.application.entity.SubOrder;
import com.paypal.api.payments.*;
import com.paypal.base.rest.*;

@Configuration
public class PaypalService implements Payable {

    private final String clientId = "Ad_7gFzA5OnbIxdYvWgYgWeopkHUW6lAwnAbrVaf9RCPVKtZbzJ9BttqNxAKj8WqxgGN4qcnEnVWsmb4";
    private final String clientSecret = "ECu6M6RZWbYAl2S4qw9nuKOVLVoLEgcps6uqoB1cn3r1Bi1bGAXrEMSOh8TewWS7xDRFnixM9WgP7byo";
    private final String mode = "sandbox";
    private String cancelUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private String returnUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

    public Map<String, String> getPaypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    @Override
    public String getToken() throws PayPalRESTException {
        OAuthTokenCredential tokenAuth = new OAuthTokenCredential(clientId, clientSecret, getPaypalSdkConfig());
        return tokenAuth.getAccessToken();
    }

    @Override
    public String getPaymentLink(String token, SubOrder subOrder) throws PayPalRESTException {
        Payment paypalPayment = createPaypalPayment(token, subOrder);
        String paymentLink = new String();

        for (Links link : paypalPayment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                paymentLink = link.getHref();
                break;
            }
        }
        return paymentLink;
    }

    public Payment createPaypalPayment(String token, SubOrder subOrder) throws PayPalRESTException {

        double finalPrice = subOrder.getFinalPrice();
        String description = subOrder.getPayerName();
        String currency = subOrder.getPaymentCurrency();

        Payment payment = getPayment(finalPrice, description, currency);

        APIContext apiContext = new APIContext(token);
        apiContext.setConfigurationMap(getPaypalSdkConfig());

        return payment.create(apiContext);
    }

    private Payment getPayment(double finalPrice, String description, String currency) {
        Amount amount = getAmount(finalPrice, currency);

        Transaction transaction = getTransaction(description, amount);

        List<Transaction> transactions = List.of(transaction);

        Payer payer = new Payer().setPaymentMethod("paypal");

        RedirectUrls redirectUrls = getRedirectUrls(cancelUrl, returnUrl);

        return getConfiguredPayment(payer, transactions, redirectUrls);
    }        

    private Amount getAmount(double finalPrice, String currency) {
        BigDecimal bigDecimalValue = new BigDecimal(finalPrice).setScale(2, RoundingMode.HALF_UP);
        String total = String.format("%.2f", bigDecimalValue).replace(',', '.');    
        return new Amount(currency, total);
    }

    private Transaction getTransaction(String description, Amount amount) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        return transaction;
    }

    private RedirectUrls getRedirectUrls(String cancelUrl, String callBackUrl) {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(callBackUrl);
        return redirectUrls;
    }

    private Payment getConfiguredPayment(Payer payer, List<Transaction> transactions, RedirectUrls redirectUrls) {
        Payment payment = new Payment("sale", payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }    
}
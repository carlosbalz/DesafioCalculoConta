package com.javachallenge.application.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class Paypal implements Payable {

    // @Value("${paypal.client.id}")
    // private String clientId;
    // @Value("${paypal.client.secret}")
    // private String clientSecret;
    // @Value("${paypal.mode}")
    // private String mode;

    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", "sandbox");
        return configMap;
    }

    @Override
    public String getToken() {
        try {
            // OAuthTokenCredential tokenAuth = new OAuthTokenCredential(clientId, clientSecret);
            OAuthTokenCredential tokenAuth = new OAuthTokenCredential(
                    "",
                    "",
                    paypalSdkConfig());
            return tokenAuth.getAccessToken();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getPaymentLink(String token, SubOrder subOrder) {
        try {
            String paymentLink = new String();
            Payment paypalPayment = createPaypalPayment(token, subOrder);
            for (Links link : paypalPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    paymentLink = link.getHref();
                    break;
                }
            }
            return paymentLink;
        } catch (Exception e) {
            return null;
        }
    }

    public Payment createPaypalPayment(String token, SubOrder subOrder) throws PayPalRESTException {
        double total = subOrder.getFinalPrice();
            String description = subOrder.getPayerName();
            String currency = subOrder.getPaymentCurrency();

            Amount amount = new Amount();
            amount.setCurrency(currency);
            total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
            amount.setTotal(String.format("%.3f", total));

            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            redirectUrls.setReturnUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");

            payment.setRedirectUrls(redirectUrls);

            APIContext apiContext = new APIContext(token);
            apiContext.setConfigurationMap(paypalSdkConfig());
            return payment.create(apiContext);
    }
}
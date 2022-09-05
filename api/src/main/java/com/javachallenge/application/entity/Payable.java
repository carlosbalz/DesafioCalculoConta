package com.javachallenge.application.entity;

public interface Payable {    

    public String getToken();

    public String getPaymentLink(String token, SubOrder subOrder);
}

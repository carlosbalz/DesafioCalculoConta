package com.javachallenge.application.Interface;

import com.javachallenge.application.entity.SubOrder;

public interface Payable {    

    public String getToken() throws Exception;

    public String getPaymentLink(String token, SubOrder subOrder) throws Exception;
}

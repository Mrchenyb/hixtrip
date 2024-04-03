package com.hixtrip.sample.entry.strategy.impl;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.entry.strategy.PaymentCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class PaymentDuplicateHandler extends PaymentCallbackHandler {


    @Override
    public String getPaymentStatus() {
        return "Duplicate";
    }

    @Override
    protected void handlePayment(Order order, CommandPayDTO dto) {
        // 执行重复支付的订单状态处理逻辑
        throw new RuntimeException("Duplicate payment, please check your payment information.");
    }
}

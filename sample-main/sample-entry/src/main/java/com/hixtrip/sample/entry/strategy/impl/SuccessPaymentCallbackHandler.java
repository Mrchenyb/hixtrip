package com.hixtrip.sample.entry.strategy.impl;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.enums.OrderPayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.entry.strategy.PaymentCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class SuccessPaymentCallbackHandler extends PaymentCallbackHandler {


    @Override
    public String getPaymentStatus() {
        return "SUCCESS";
    }

    @Override
    protected void handlePayment(Order order, CommandPayDTO dto) {
        // 执行更新支付成功的订单状态
        if (order.getPayStatus().equals(OrderPayStatusEnum.CREATED.name())) {
            orderService.orderPaySuccess(order.getId(), OrderPayStatusEnum.PAID);
        }
    }
}

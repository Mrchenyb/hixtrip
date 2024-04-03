package com.hixtrip.sample.entry.strategy.impl;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.enums.OrderPayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.entry.strategy.PaymentCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class FailedPaymentCallbackHandler extends PaymentCallbackHandler {

    @Override
    public String getPaymentStatus() {
        return "FAILED";
    }

    @Override
    protected void handlePayment(Order order, CommandPayDTO dto) {
        // 更新支付失败的订单状态
        if (order.getPayStatus().equals(OrderPayStatusEnum.CREATED.name())) {
            orderService.orderPayFail(order.getId(), OrderPayStatusEnum.CANCELLED);
        }
    }
}

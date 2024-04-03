package com.hixtrip.sample.entry.strategy;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.enums.OrderPayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class PaymentCallbackHandler {

    @Autowired
    protected OrderService orderService;

    void handlePaymentCallback(CommandPayDTO dto) {
        // 执行支付回调前置操作，如检查参数，更新订单状态等
        // 这里省略了一些代码
        // 查找订单， 验证参数等
        Order order = orderService.getById(dto.getOrderId());
        if (!order.getPayStatus().equals(OrderPayStatusEnum.CREATED.name())) {
            return;
        }

        //  这里应该是根据不同的支付方式，调用不同的支付回调处理逻辑
        this.handlePayment(order, dto);
    }

    protected abstract String getPaymentStatus();

    protected abstract void handlePayment(Order order,CommandPayDTO dto);
}

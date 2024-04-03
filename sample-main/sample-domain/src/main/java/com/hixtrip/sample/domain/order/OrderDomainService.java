package com.hixtrip.sample.domain.order;

import com.hixtrip.sample.domain.order.enums.OrderPayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 订单领域服务
 * todo 只需要实现创建订单即可
 */
@Component
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    /**
     * todo 需要实现
     * 创建待付款订单
     */
    public void createOrder(Order order) {

        order.setPayStatus(OrderPayStatusEnum.CREATED.name());
        // 创建订单
        Boolean save = orderRepository.save(order);
        if (!save) {
            throw new RuntimeException("Order save failed");
        }
    }

    /**
     * todo 需要实现
     * 待付款订单支付成功
     */
    public void orderPaySuccess(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参
        OrderPayStatusEnum orderPayStatusEnum = OrderPayStatusEnum.fromString(commandPay.getPayStatus());
        if (orderPayStatusEnum == null) {
            throw new RuntimeException("Order pay status not exist");
        }
        if (!orderRepository.updateOrderPayStatus(commandPay)) {
            throw  new RuntimeException("Order pay success failed");
        }
    }

    /**
     * todo 需要实现
     * 待付款订单支付失败
     */
    public void orderPayFail(CommandPay commandPay) {
        //需要你在infra实现, 自行定义出入参
        OrderPayStatusEnum orderPayStatusEnum = OrderPayStatusEnum.fromString(commandPay.getPayStatus());
        if (orderPayStatusEnum == null) {
            throw new RuntimeException("Order payFail status not exist");
        }
        if (!orderRepository.updateOrderPayStatus(commandPay)) {
            throw  new RuntimeException("Order pay fail failed");
        }
    }
}

package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.domain.order.enums.OrderPayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;

/**
 * 订单的service层
 */
public interface OrderService {


    /**
     * 创建订单
     * @param commandOderCreateDTO
     */
    void createOrder(CommandOderCreateDTO commandOderCreateDTO);

    /**
     * 根据订单id获取订单
     * @param orderId
     * @return
     */
    Order getById(String orderId);

    /**
     * 支付成功处理
     * @param id
     * @param orderPayStatusEnum
     */
    void orderPaySuccess(String id, OrderPayStatusEnum orderPayStatusEnum);

    /**
     * 支付失败处理
     * @param id
     * @param orderPayStatusEnum
     */
    void orderPayFail(String id, OrderPayStatusEnum orderPayStatusEnum);
}

package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;

/**
 *
 */
public interface OrderRepository {
    Boolean updateOrderPayStatus(CommandPay commandPay);

    Boolean save(Order order);
}

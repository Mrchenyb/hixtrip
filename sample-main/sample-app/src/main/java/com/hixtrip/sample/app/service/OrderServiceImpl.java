package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.enums.OrderPayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * app层负责处理request请求，调用领域服务
 *
 *
 */
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDomainService orderDomainService;

    private final InventoryDomainService inventoryDomainService;

    private final CommodityDomainService commodityDomainService;

    private final PayDomainService payDomainService;


    @Override
    public void createOrder(CommandOderCreateDTO commandOderCreateDTO) {
        Order order = OrderConvertor.INSTANCE.dto2Domain(commandOderCreateDTO);
        Integer inventory = inventoryDomainService.getInventory(order.getSkuId());
        if (inventory < order.getAmount()) {
            throw new RuntimeException("Inventory not enough");

        }
        Boolean result = inventoryDomainService.changeInventory(order.getSkuId(), new Long(inventory), new Long(order.getAmount()), new Long(order.getAmount()));
        if (!result) {
            throw new RuntimeException("Inventory deduction failed");

        }
        BigDecimal skuPrice = commodityDomainService.getSkuPrice(order.getSkuId());
        if (skuPrice == null) {
            throw new RuntimeException("Sku not found");
        }
        order.setMoney(skuPrice.multiply(new BigDecimal(order.getAmount())));
        orderDomainService.createOrder(order);
    }

    @Override
    public Order getById(String orderId) {
        // 查找订单
        return new Order();
    }

    @Override
    public void orderPaySuccess(String id, OrderPayStatusEnum orderPayStatusEnum) {
        CommandPay commandPay = CommandPay.builder().payStatus(orderPayStatusEnum.name()).orderId(id).build();
        orderDomainService.orderPaySuccess(commandPay);
        payDomainService.payRecord(commandPay);
    }

    @Override
    public void orderPayFail(String id, OrderPayStatusEnum orderPayStatusEnum) {
        CommandPay commandPay = CommandPay.builder().payStatus(orderPayStatusEnum.name()).orderId(id).build();
        orderDomainService.orderPayFail(commandPay);
        payDomainService.payRecord(commandPay);
    }
}

package com.hixtrip.sample.entry.strategy;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentCallbackContext {
    private Map<String, PaymentCallbackHandler> handlers = new HashMap<>();

    public PaymentCallbackContext(List<PaymentCallbackHandler> list) {
        list.forEach(handler -> handlers.put(handler.getPaymentStatus(), handler));
    }

    public void handleCallback(CommandPayDTO dto) {
        PaymentCallbackHandler handler = handlers.get(dto.getPayStatus());
        if (handler != null) {
            handler.handlePaymentCallback(dto);
        } else {
            // 处理未知类型或未找到处理器的情况
            throw new IllegalArgumentException("Unknown payment callback status " + dto.getPayStatus());
        }
    }
}

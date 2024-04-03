package com.hixtrip.sample.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderPayStatusEnum {

    CREATED,
    PAID,
    CANCELLED,
    COMPLETED;

    public static OrderPayStatusEnum fromString(String value) {
        for (OrderPayStatusEnum status : OrderPayStatusEnum.values()) {
            if (status.toString().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}


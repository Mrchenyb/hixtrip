package com.hixtrip.sample.domain.inventory;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 库存领域服务
 * 库存设计，忽略仓库、库存品、计量单位等业务
 */
@Component
@RequiredArgsConstructor
public class InventoryDomainService {

    private final InventoryRepository inventoryRepository;

    /**
     * 获取sku当前库存
     *
     * @param skuId
     */
    public Integer getInventory(String skuId) {
        //todo 需要你在infra实现，只需要实现缓存操作, 返回的领域对象自行定义
        return inventoryRepository.getInventory(skuId);
    }

    /**
     * 修改库存
     *
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return
     */
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        //todo 需要你在infra实现，只需要实现缓存操作。
        if (sellableQuantity > 0) {
            inventoryRepository.setInventory(skuId, sellableQuantity);
        }
        // 没太理解后两个参数的作用，可能预占先扣除 redis， 占用 去记录对应的db操作
        if (withholdingQuantity > 0) {
            inventoryRepository.setWithholdingInventory(skuId, withholdingQuantity);
        }
        // 所以两个参数我都去redis里面扣减了
        if (occupiedQuantity > 0) {
            inventoryRepository.setOccupiedInventory(skuId, occupiedQuantity);
        }
        return true;
    }
}

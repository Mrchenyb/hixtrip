package com.hixtrip.sample.domain.inventory.repository;

/**
 *
 */
public interface InventoryRepository {

    /**
     * 获取sku库存
     *
     * @param skuId
     * @return
     */
    Integer getInventory(String skuId);


    /**
     * 设置sku库存
     *
     * @param skuId
     * @param sellableQuantity
     */
    void setInventory(String skuId, Long sellableQuantity);

    /**
     * 预扣库存
     *
     * @param skuId
     * @param withholdingQuantity
     */
    void setWithholdingInventory(String skuId, Long withholdingQuantity);

    /**
     * 扣减库存
     *
     * @param skuId
     * @param occupiedQuantity
     */
    void setOccupiedInventory(String skuId, Long occupiedQuantity);
}

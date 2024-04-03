package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DefaultRedisScript<Long> decrementStockScript;

    private String keys = "sku:";

    @Override
    public Integer getInventory(String skuId) {
        return (Integer) redisTemplate.opsForValue().get(keys + skuId);
    }

    @Override
    public void setInventory(String skuId, Long sellableQuantity) {
        try {
            redisTemplate.opsForValue().set(keys + skuId, sellableQuantity);
        } catch (Exception e) {
            throw new RuntimeException("set inventory failed");
        }
    }

    @Override
    public void setWithholdingInventory(String skuId, Long withholdingQuantity) {
        // 实现扣减库存
        decrementStockScript.setScriptText("local key = KEYS[1] -- 商品ID\n" +
                "local quantity = tonumber(ARGV[1]) -- 要扣减的库存数量\n" +
                "\n" +
                "-- 获取当前库存\n" +
                "local current_stock = redis.call('GET', key)\n" +
                "\n" +
                "-- 库存不足，返回错误\n" +
                "if current_stock < quantity then\n" +
                "    return 0\n" +
                "end\n" +
                "\n" +
                "-- 扣减库存\n" +
                "redis.call('DECRBY', key, quantity)\n" +
                "\n" +
                "-- 返回扣减后的剩余库存作为校验结果\n" +
                "return current_stock - quantity");

        // 执行Lua脚本，返回扣减后的剩余库存
        Long remainingStock = redisTemplate.execute(decrementStockScript, (List<String>) Collections.singleton(keys + skuId), Collections.singleton(withholdingQuantity));

        if (remainingStock == null || remainingStock == 0) {
            throw new RuntimeException("set withholding inventory failed, remaining stock is 0");
        }
    }

    @Override
    public void setOccupiedInventory(String skuId, Long occupiedQuantity) {
        // 实现扣减库存
        decrementStockScript.setScriptText("local key = KEYS[1] -- 商品ID\n" +
                "local quantity = tonumber(ARGV[1]) -- 要扣减的库存数量\n" +
                "\n" +
                "-- 获取当前库存\n" +
                "local current_stock = redis.call('GET', key)\n" +
                "\n" +
                "-- 库存不足，返回错误\n" +
                "if current_stock < quantity then\n" +
                "    return 0\n" +
                "end\n" +
                "\n" +
                "-- 扣减库存\n" +
                "redis.call('DECRBY', key, quantity)\n" +
                "\n" +
                "-- 返回扣减后的剩余库存作为校验结果\n" +
                "return current_stock - quantity");
        // 执行Lua脚本，返回扣减后的剩余库存
        Long remainingStock = redisTemplate.execute(decrementStockScript, (List<String>) Collections.singleton(keys + skuId), Collections.singleton(occupiedQuantity));

        if (remainingStock == null || remainingStock == 0) {
            throw new RuntimeException("set withholding inventory failed, remaining stock is 0");
        }

    }
}

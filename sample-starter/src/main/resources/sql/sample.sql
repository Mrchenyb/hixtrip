CREATE TABLE orders
(
    order_id          BIGINT PRIMARY KEY,                                          -- 订单ID，唯一标识每个订单
    buyer_id          BIGINT         NOT NULL,                                     -- 买家ID
    seller_id         BIGINT         NOT NULL,                                     -- 卖家ID
    order_status      ENUM('CREATED', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELED'), -- 订单状态
    order_time        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,           -- 订单创建时间
    order_amount      DECIMAL(10, 2) NOT NULL,                                     -- 订单金额
    order_tail_number VARCHAR(50)    NOT NULL,                                     -- 订单尾号，用于客服搜索
    buyer_name        VARCHAR(100)   NOT NULL,                                     -- 买家姓名，用于客服搜索
    -- 其他必要字段...
    INDEX             idx_buyer_id (buyer_id),                                     -- 买家ID索引，用于买家查询订单
    INDEX             idx_seller_id (seller_id),                                   -- 卖家ID索引，用于卖家查询订单
    INDEX             idx_order_time (order_time),                                 -- 订单时间索引，用于按时间范围搜索订单
    INDEX             idx_order_tail_number (order_tail_number)                    -- 订单尾号索引，用于客服搜索订单
);
-- 原则上， 我们通过mysql+ 分库分表的形式，按照用户id， 或者建立路由表，能达到大数据量存储的要求。不过鉴于可以自定义存储方案，建议采用分布式关系行数据库

--1.由于买家查询订单的实时性要求高，我们将直接从分布式关系型数据库中查询数据。
-- 利用buyer_id索引加速查询，确保买家能够快速获取到自己的订单列表。
-- 考虑到实时性，不建议使用缓存，以避免数据不一致的问题。

--2.卖家查询订单允许秒级延迟，因此可以考虑使用缓存策略来减轻数据库压力。
-- 当卖家发起查询时，首先检查缓存中是否有相关订单数据，如果有则直接返回；否则，从数据库中查询并将结果存入缓存。
-- 利用seller_id索引来加速查询

--3.由于客服搜索客诉订单允许分钟级延迟，我们可以采用异步搜索或定时批处理的方式来处理这类查询。
--  将搜索请求放入队列中，由后台任务定期处理并更新搜索结果到缓存或搜索引擎中。
--  使用order_time、order_tail_number和buyer_name索引来加速查询。
--  客服可以直接查询缓存或搜索引擎来获取搜索结果。

-- 4.平台运营进行订单数据分析：
-- 数据分析通常不需要实时性，可以将订单数据定期同步到数据仓库或离线分析系统中。
-- 在数据仓库中，可以利用OLAP技术进行复杂的数据分析，如计算买家订单排行榜、卖家订单排行榜等。
-- 使用SQL或大数据分析工具（如Flink）来完成数据分析任务。

-- 同时建立对应的监控报警机制
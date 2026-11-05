package com.flashmall.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 雪花算法 ID 生成器。
 *
 * 64 位结构：
 * | 1 符号位(恒0) | 41 时间戳(毫秒) | 5 机房ID | 5 机器ID | 12 序列号 |
 */
@Component
public class SnowflakeIdGenerator {

    /** 起始纪元：2020-01-01 00:00:00 UTC+8 */
    private static final long EPOCH = 1577808000000L;

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);          // 31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);  // 31
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);           // 4095

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;                                              // 12
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;                         // 17
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;        // 22

    /** 时钟回拨容忍上限（毫秒），超过直接抛异常 */
    private static final long MAX_BACKWARD_MS = 5L;

    @Value("${snowflake.worker-id:0}")
    private long workerId;

    @Value("${snowflake.datacenter-id:0}")
    private long datacenterId;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator() {
    }

    /** 供单测/手动 new 时使用，Spring 环境走 @Value 注入 */
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("workerId 必须在 0~" + MAX_WORKER_ID + " 之间");
        }
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException("datacenterId 必须在 0~" + MAX_DATACENTER_ID + " 之间");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 生成下一个全局唯一 ID。
     * synchronized 保证单实例内 sequence/lastTimestamp 不被并发改乱。
     */
    public synchronized long nextId(){
        long now =System.currentTimeMillis();

        if (now< lastTimestamp) {
            long back = lastTimestamp -now;
            if (back <=MAX_BACKWARD_MS) {
                while (now< lastTimestamp) {
                    now = System.currentTimeMillis();
                }
            }else{
                throw new IllegalStateException("时钟回拨"+back+"ms,拒绝生成ID");
            }
        }
        if (now ==lastTimestamp) {
            sequence = (sequence +1)& SEQUENCE_MASK;
            if (sequence ==0) {
                now = waitNextMillis(lastTimestamp);
            }
        }else{
            sequence = 0L;
        }
        lastTimestamp = now;
        return ((now -EPOCH)<<TIMESTAMP_SHIFT)
        |(datacenterId<<DATACENTER_ID_SHIFT)
        |(workerId<<WORKER_ID_SHIFT)
        | sequence;
    }
    private long waitNextMillis (long lastTimestamp){
        long now = System.currentTimeMillis();
        while (now <= lastTimestamp) {
            now = System.currentTimeMillis();
        }
        return  now;
    }
}
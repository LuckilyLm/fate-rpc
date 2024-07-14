package org.fate.faterpc.fault.retry;

import org.fate.faterpc.spi.SpiLoader;

/**
 * 重试策略工厂
 * @Author: Fate
 * @Date: 2024/7/13 16:14
 **/
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 获取默认的重试策略
     */
    private static final RetryStrategy DEFAULT_STRATEGY = new NoRetryStrategy();

    /**
     * 获取重试策略实例
     * @param key 策略key
     * @return 重试策略实例
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}

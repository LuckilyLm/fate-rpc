package org.fate.faterpc.fault.tolerant;

import org.fate.faterpc.fault.retry.RetryStrategy;
import org.fate.faterpc.spi.SpiLoader;

/**
 * 容错策略工厂
 * @Author: Fate
 * @Date: 2024/7/13 16:14
 **/
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategyFactory.class);
    }

    /**
     * 获取默认的容错策略
     */
    private static final TolerantStrategy DEFAULT_STRATEGY = new FailSafeTolerantStrategy();

    /**
     * 获取容错策略实例
     * @param key 策略key
     * @return 容错策略实例
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}

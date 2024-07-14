package org.fate.faterpc.fault.retry;

/**
 * 重试策略键
 * @Author: Fate
 * @Date: 2024/7/13 16:12
 **/
public interface RetryStrategyKeys
{
    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定间隔重试
     */
    String FIXED_INTERVAL = "fixedInterval";
}

package org.fate.faterpc.fault.tolerant;

/**
 * @Author: Fate
 * @Date: 2024/7/13 16:41
 **/
public interface TolerantStrategyKeys
{
    /**
     * 故障转移策略：失败切换
     */
    String FAIL_OVER = "failOver";


    /**
     * 快速失败策略：快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 静默策略：静默失败
     */
    String FAIL_SAFE = "failSafe";

    /**
     * 故障恢复策略：失败自动恢复
     */
    String FAIL_BACK = "failBack";
}

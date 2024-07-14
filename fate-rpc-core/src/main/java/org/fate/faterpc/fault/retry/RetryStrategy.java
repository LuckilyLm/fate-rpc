package org.fate.faterpc.fault.retry;

import org.fate.faterpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略接口
 * @Author: Fate
 * @Date: 2024/7/13 16:00
 **/
public interface RetryStrategy
{
    /**
     * 执行重试
     * @param callable Callable
     * @return RpcResponse
     * @throws Exception 异常
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}

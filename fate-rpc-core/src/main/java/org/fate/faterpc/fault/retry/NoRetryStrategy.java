package org.fate.faterpc.fault.retry;

import org.fate.faterpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 * @Author: Fate
 * @Date: 2024/7/13 16:02
 **/
public class NoRetryStrategy implements RetryStrategy {

    /**
     * 不重试策略
     * @param callable Callable 接口
     * @return 直接返回 callable.call() 的结果
     * @throws Exception 异常
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}

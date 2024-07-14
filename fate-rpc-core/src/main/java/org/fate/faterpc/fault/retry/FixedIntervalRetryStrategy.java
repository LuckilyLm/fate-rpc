package org.fate.faterpc.fault.retry;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定间隔重试策略
 * @Author: Fate
 * @Date: 2024/7/13 16:03
 **/
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {
    @Override
    @SuppressWarnings("all")
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {

        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.warn("重试次数:{}, 异常信息:{}", attempt.getAttemptNumber(), attempt.getExceptionCause().getMessage());
                    }
                }).build();
        return retryer.call(callable);
    }
}

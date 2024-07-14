package org.fate.faterpc.fault.tolerant;

import org.fate.faterpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败策略，即遇到错误立即返回
 * @Author: Fate
 * @Date: 2024/7/13 16:34
 **/
public class FailFastTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务调用失败",e);
    }
}

package org.fate.faterpc.fault.tolerant;

import org.fate.faterpc.model.RpcResponse;

import java.util.Map;

/**
 * 转移策略：失败转移
 * @Author: Fate
 * @Date: 2024/7/13 16:34
 **/
public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO
        throw new RuntimeException("服务调用失败",e);
    }
}

package org.fate.faterpc.fault.tolerant;

import org.fate.faterpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 * @Author: Fate
 * @Date: 2024/7/13 16:32
 **/
public interface TolerantStrategy
{
    RpcResponse doTolerant(Map<String, Object> context,Exception e);
}

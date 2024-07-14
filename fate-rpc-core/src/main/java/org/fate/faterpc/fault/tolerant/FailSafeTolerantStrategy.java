package org.fate.faterpc.fault.tolerant;

import lombok.extern.slf4j.Slf4j;
import org.fate.faterpc.model.RpcResponse;

import java.util.Map;

/**
 * 静默容错策略
 * @Author: Fate
 * @Date: 2024/7/13 16:36
 **/
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.error("静默异常处理",e);
        return new RpcResponse();
    }
}

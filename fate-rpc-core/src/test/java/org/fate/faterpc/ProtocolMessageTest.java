package org.fate.faterpc;

/**
 * @Author: Fate
 * @Date: 2024/7/12 22:44
 **/

import cn.hutool.core.util.IdUtil;
import io.vertx.core.buffer.Buffer;
import org.fate.faterpc.constant.ProtocolConstant;
import org.fate.faterpc.constant.RpcConstant;
import org.fate.faterpc.model.RpcRequest;
import org.fate.faterpc.protocol.*;
import org.junit.Assert;
import org.junit.Test;


public class ProtocolMessageTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        // 构造消息
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();

        ProtocolMessage.Header header = getHeader();

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa", "bbb"});
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?> message = ProtocolMessageDecoder.decode(encodeBuffer);
        Assert.assertNotNull(message);
    }

    private static ProtocolMessage.Header getHeader() {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        return header;
    }

}
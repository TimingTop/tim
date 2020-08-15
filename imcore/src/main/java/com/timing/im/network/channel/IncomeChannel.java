package com.timing.im.network.channel;

import com.timing.im.network.endpoint.RawData;
import com.timing.im.protocol.coap.CoAP;
import com.timing.im.protocol.coap.Message;
import com.timing.im.protocol.coap.Request;
import com.timing.im.protocol.coap.codec.CoAPDecoder;
import com.timing.im.protocol.coap.codec.Decoder;

import java.util.concurrent.ExecutorService;

public class IncomeChannel implements RawDataChannel {

    // 这个是   协议处理栈 与 connector 沟通类，
    // endpoint 是整个 跟 业务无关的 协议处理类
    // 需要一个 协议栈 处理 rawData, 从 endpoint 获取
    // 需要一个 线程池， 从 endpoint 获取

    private ExecutorService executor;  // 从 endpoint 拿，会比较好控制

    private Decoder parser; // 从 endpoint 拿，比较好管理




    @Override
    public void receiveData(RawData rawData) {
        // 用协议栈处理 rawData

        // 用线程池 处理，
        executor.execute(() -> {
            receiveMessage(rawData);
        });


    }

    private void receiveMessage(final RawData raw) {
        // 把 raw 解析成 协议相关的 message
        // 先把 coap 协议当做 demo 进行

        Message message = parser.parseMessage(raw);

        // 根据 code 判断一下 该数据包是 请求还是返回 或者 空 message



    }

    private void receiveRequest(final Request request) {

    }
}

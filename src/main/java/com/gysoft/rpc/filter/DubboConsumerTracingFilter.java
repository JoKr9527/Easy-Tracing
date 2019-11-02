package com.gysoft.rpc.filter;

import brave.ScopedSpan;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import com.gysoft.trace.LocalTracing;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Map;

/**
 * dubbo 消费者拦截器
 * @author duofei
 * @date 2019/10/25
 */
@Activate(group = {Constants.CONSUMER})
public class DubboConsumerTracingFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Tracing tracing = LocalTracing.get();
        // 对外提供接口的 dubbo 服务只作为 consumer
        final TraceContext.Injector<Map<String, String>> injector = tracing.propagation().injector(new Propagation.Setter<Map<String, String>, String>() {
            @Override
            public void put(Map<String, String> attachments, String key, String value) {
                attachments.put(key, value);
            }

            @Override
            public String toString() {
                return "DubboClientRequest::putAttachment";
            }
        });
        Tracer tracer = tracing.tracer();
        ScopedSpan span = tracer.startScopedSpan("调用 " + invoker.getInterface().getName() + "接口的 " + invocation.getMethodName() + "()");
        injector.inject(span.context(), RpcContext.getContext().getAttachments());
        Result result = invoker.invoke(invocation);
        span.finish();
        return result;
    }
}

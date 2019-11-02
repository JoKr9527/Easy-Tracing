package com.gysoft.rpc.filter;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import com.gysoft.trace.LocalTracing;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Map;

/**
 * dubbo 提供者拦截器
 * @author duofei
 * @date 2019/10/28
 */
@Activate(group = {Constants.PROVIDER})
public class DubboProviderTracingFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Tracing tracing = LocalTracing.localTracing("服务名 " + invoker.getUrl().getParameters().get("application"), invoker.getUrl().getPort());
        final Tracer tracer = tracing.tracer();
        final TraceContext.Extractor<Map<String, String>> extractor = tracing.propagation().extractor(new Propagation.Getter<Map<String, String>, String>() {
            @Override
            public String get(Map<String, String> attachments, String key) {
                return attachments.get(key);
            }

            @Override
            public String toString() {
                return "DubboServerRequest::getAttachment";
            }
        });

        final TraceContextOrSamplingFlags extract = extractor.extract(RpcContext.getContext().getAttachments());
        Span span = null;
        if(extract.context() != null){
            span = tracer.joinSpan(extract.context()).name(invocation.getMethodName() + "() 执行");
        }else {
            span = tracer.nextSpan(extract).name(invocation.getMethodName() + "() 执行");
        }
        final Tracer.SpanInScope spanInScope = tracer.withSpanInScope(span);
        final ScopedSpan scopedSpan = tracer.startScopedSpan(invocation.getMethodName() + "() 执行");
        Result result = invoker.invoke(invocation);
        scopedSpan.finish();
        spanInScope.close();
        tracing.close();
        return result;
    }
}

package com.gysoft.rpc.filter;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;
import com.gysoft.trace.CurrentTraceContextFactory;
import com.gysoft.trace.LocalTracing;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author duofei
 * @date 2019/10/25
 */
@Activate(group = {Constants.CONSUMER})
public class DubboTracingFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Tracing tracing = LocalTracing.get();
        Tracer tracer = tracing.tracer();
        ScopedSpan span = tracer.startScopedSpan("我执行了" + invoker.getInterface().getName());
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        System.out.println("我执行了这么久呢：" + (System.currentTimeMillis() - start));
        span.finish();
        return result;
    }
}

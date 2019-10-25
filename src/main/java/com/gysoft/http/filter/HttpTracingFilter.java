package com.gysoft.http.filter;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.gysoft.trace.CurrentTraceContextFactory;
import com.gysoft.trace.LocalTracing;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author duofei
 * @date 2019/10/25
 */
@WebFilter(urlPatterns = {"/*"})
public class HttpTracingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Tracing tracing = LocalTracing.localTracing("192.168.3.18", 9411);
        ScopedSpan span = tracing.tracer().startScopedSpan("我开始调用接口啦！！");
        chain.doFilter(request,response);
        span.finish();
        tracing.close();
    }

    @Override
    public void destroy() {

    }
}

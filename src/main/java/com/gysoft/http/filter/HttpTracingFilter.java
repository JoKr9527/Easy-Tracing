package com.gysoft.http.filter;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import com.gysoft.trace.CurrentTraceContextFactory;
import com.gysoft.trace.LocalTracing;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

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
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        Tracing tracing = LocalTracing.localTracing("服务名 " + httpServletRequest.getContextPath(), httpServletRequest.getServerPort());
        ScopedSpan span = tracing.tracer().startScopedSpan("web请求路径 " + httpServletRequest.getServletPath());
        chain.doFilter(request,response);
        span.finish();
        tracing.close();
    }

    @Override
    public void destroy() {

    }
}

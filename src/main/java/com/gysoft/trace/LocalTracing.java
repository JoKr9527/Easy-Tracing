package com.gysoft.trace;

import brave.Tracing;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

/**
 * 基于 threadLocalCurrentTraceContext 生成的 Tracing
 * @author duofei
 * @date 2019/10/25
 */
public class LocalTracing {

    private static final ThreadLocal<Tracing> localTracing = new ThreadLocal<>();

    /**
     * 获取 tracing；同一线程环境下，该方法不应多次调用
     * @author duofei
     * @date 2019/10/25
     * @param localServiceName 服务名
     * @param localPort 本地端口
     * @return Tracing 线程上下文的Tracing
     */
    public static Tracing localTracing(String localServiceName, Integer localPort){
        Tracing tracing = Tracing.newBuilder().currentTraceContext(CurrentTraceContextFactory.get())
                .spanReporter(AsyncReporter.create(URLConnectionSender.create("http://localhost:9411/api/v2/spans")))
                .localServiceName(localServiceName).localPort(localPort).build();
        localTracing.set(tracing);
        return tracing;
    }

    /**
     * 获取 tracing；同一线程环境下，该方法不应多次调用
     * @author duofei
     * @date 2019/10/25
     * @return Tracing 线程上下文的Tracing
     */
    public static Tracing localTracing(){
        Tracing tracing = Tracing.newBuilder().currentTraceContext(CurrentTraceContextFactory.get()).build();
        localTracing.set(tracing);
        return tracing;
    }

    /**
     * 返回当前线程环境下的 tracing
     * @author duofei
     * @date 2019/10/25
     * @return  Tracing; NULL if localTracing() not used
     */
    public static Tracing get() {
        return localTracing.get();
    }
}

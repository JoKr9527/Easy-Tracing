package com.gysoft.trace;

import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;

/**
 * currentTraceContext 工厂类
 * @author duofei
 * @date 2019/10/25
 */
public class CurrentTraceContextFactory {

    private static final ThreadLocal<CurrentTraceContext> localCurrentTraceContext = new ThreadLocal<>();

    private CurrentTraceContextFactory() {

    }

    /**
     * 构建 threadLocalCurrentTraceContext
     * @author duofei
     * @date 2019/10/25
     * @param scopeDecorator scope decorator
     * @return CurrentTraceContext threadLocalCurrentTraceContext 实例
     */
    public static CurrentTraceContext threadLocalCurrentTraceContext(CurrentTraceContext.ScopeDecorator scopeDecorator){
        CurrentTraceContext currentTraceContext;
        if(scopeDecorator != null){
            currentTraceContext = ThreadLocalCurrentTraceContext.newBuilder().addScopeDecorator(scopeDecorator).build();
        }else{
            currentTraceContext = ThreadLocalCurrentTraceContext.create();
        }
        localCurrentTraceContext.set(currentTraceContext);
        return currentTraceContext;
    }

    /**
     * 获取当前线程环境的 currentTraceContext
     * @author duofei
     * @date 2019/10/25
     * @return 当前线程环境的 currentTraceContext
     */
    public static CurrentTraceContext get() {
        if(localCurrentTraceContext.get() == null){
            localCurrentTraceContext.set(threadLocalCurrentTraceContext(null));
        }
        return localCurrentTraceContext.get();
    }
}

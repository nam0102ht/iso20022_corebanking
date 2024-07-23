package com.ntnn.concurrent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class MdcAwareCallableDecorator<T> implements Callable<T> {
    @Getter
    private final Callable<T> callable;
    @Getter
    private final Map contextMap;

    public MdcAwareCallableDecorator(Callable<T> callable) {
        this.contextMap = MDC.getCopyOfContextMap();
        this.callable = callable;
        log.trace("this=\"{}\", current_thread.name=\"{}\", context_map=\"{}\"", this, Thread.currentThread().getName(), this.contextMap);
    }

    @Override
    public T call() throws Exception {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
            log.trace("this=\"{}\", current_thread.name=\"{}\", context_map=\"{}\"", this, Thread.currentThread().getName(), this.contextMap);
        } else {
            log.error("message=\"MDC contextMap is null. {} objects must have at least the correlation ID\"", this.getClass().getName());
        }
        try {
            return callable.call();
        } finally {
            MDC.clear();
        }
    }

    @Override
    public String toString() {
        return (new JSONObject(contextMap)).toString();
    }
}

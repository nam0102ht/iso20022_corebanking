package com.ntnn.concurrent;

import java.util.concurrent.Callable;

public class MdcAwareCallableService implements CallableService {
    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        return new MdcAwareCallableDecorator<T>(callable);
    }
}

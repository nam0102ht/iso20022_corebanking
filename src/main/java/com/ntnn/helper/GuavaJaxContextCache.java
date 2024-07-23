package com.ntnn.helper;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.prowidesoftware.swift.model.mx.AbstractMX;
import com.prowidesoftware.swift.model.mx.JaxbContextCache;
import jakarta.xml.bind.JAXBContext;

import java.util.concurrent.ExecutionException;

public class GuavaJaxContextCache implements JaxbContextCache {
    private final Cache<Class<? extends AbstractMX>, JAXBContext> cache = CacheBuilder.newBuilder().maximumSize(100).build();

    @Override
    public JAXBContext get(final Class messageClass, final Class<?>[] classes) throws ExecutionException {
        return cache.get(messageClass, () -> JAXBContext.newInstance(classes));
    }

}

package io.renren.http;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author Administrator
 */
public class HttpClientFactoryBean<T> implements FactoryBean {

    private Class<T> interfaceClazz;

    public HttpClientFactoryBean(Class<T> interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    @Override
    public Object getObject() throws Exception {
        HttpClientProxy httpClientProxy = new HttpClientProxy();
        final T t = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceClazz}, httpClientProxy);
        return t;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

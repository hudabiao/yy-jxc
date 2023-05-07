package io.renren.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpClientProxy implements InvocationHandler {

    private String url;
    private String method;
    private Object args;

    public HttpClientProxy() {
    }

    public HttpClientProxy(String url, String method, Object args) {
        this.url = url;
        this.method = method;
        this.args = args;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始执行http请求");

        return "执行结果";
    }
}

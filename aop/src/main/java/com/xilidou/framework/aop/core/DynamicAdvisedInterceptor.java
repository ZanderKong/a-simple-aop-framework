package com.xilidou.framework.aop.core;

import com.xilidou.framework.aop.Invocation.CglibMethodInvocation;
import com.xilidou.framework.aop.Invocation.MethodInvocation;
import com.xilidou.framework.aop.advisor.TargetSource;
import com.xilidou.framework.aop.interceptor.AopMethodInterceptor;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class DynamicAdvisedInterceptor implements MethodInterceptor{

    //定义了两个成员变量，interceptorList和targetSource，
    // interceptorList是拦截器链列表，
    // targetSource是目标对象的封装。
    protected final List<AopMethodInterceptor> interceptorList;
    protected final TargetSource targetSource;

    //定义了DynamicAdvisedInterceptor类的构造函数，用于初始化interceptorList和targetSource成员变量。
    public DynamicAdvisedInterceptor(List<AopMethodInterceptor> interceptorList, TargetSource targetSource) {
        this.interceptorList = interceptorList;
        this.targetSource = targetSource;
    }

    //实现了MethodInterceptor接口的intercept方法，用于实现拦截器链的调用和目标方法的调用。
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        MethodInvocation invocation = new CglibMethodInvocation(obj,targetSource.getTagetObject(),method, args,interceptorList,proxy);
        return invocation.proceed();
    }
}
//需要注意的是，在使用cglib库进行动态代理时，需要保证被代理的类没有被final修饰，否则cglib将无法生成代理类。

package com.xilidou.framework.aop.core;

import com.xilidou.framework.aop.advisor.AdvisedSupport;
import com.xilidou.framework.ioc.utils.ClassUtils;
import lombok.Data;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

@Data
public class CglibAopProxy implements AopProxy{

    //定义了三个变量，分别表示被代理对象的支持、构造函数的参数和类型，以及一个构造函数，接受一个AdvisedSupport对象的参数。
    private AdvisedSupport advised;

    private Object[] constructorArgs;

    private Class<?>[] constructorArgTypes;

    public CglibAopProxy(AdvisedSupport config){
        this.advised = config;
    }



    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {

        //获取被代理类的Class对象
        Class<?> rootClass = advised.getTargetSource().getTagetClass();
        //使用Enhancer创建一个代理类，设置其父类为被代理类的父类，设置其回调方法为getCallBack返回的回调方法，设置其类加载器为传入的类加载器（或默认加载器），最后进行代理类的构建并返回。
        if(classLoader == null){
            classLoader = ClassUtils.getDefultClassLoader();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(rootClass.getSuperclass());
        //增加拦截器的核心方法
        Callback callbacks = getCallBack(advised);
        enhancer.setCallback(callbacks);
        enhancer.setClassLoader(classLoader);
        if(constructorArgs != null && constructorArgs.length > 0){
            return enhancer.create(constructorArgTypes,constructorArgs);
        }

        return enhancer.create();
    }
    //返回了一个回调方法，这个回调方法会被Enhancer使用。回调方法的作用是在代理对象的方法被调用时，根据AdvisedSupport中的拦截器链，依次调用拦截器对应的方法。
    private Callback getCallBack(AdvisedSupport advised) {
        return new DynamicAdvisedInterceptor(advised.getList(),advised.getTargetSource());
    }
}

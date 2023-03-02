package com.xilidou.framework.aop.core;

import com.xilidou.framework.aop.adapter.AfterRunningAdviceAdapter;
import com.xilidou.framework.aop.adapter.BeforeMethodAdviceAdapter;
import com.xilidou.framework.aop.advisor.*;
import com.xilidou.framework.aop.bean.AopBeanDefinition;
import com.xilidou.framework.aop.interceptor.AopMethodInterceptor;
import com.xilidou.framework.ioc.core.BeanFactoryImpl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhengxin
 */
public class AopBeanFactoryImpl extends BeanFactoryImpl{

    //定义ConcurrentHashMap变量aopBeanDefinitionMap，用于存放AopBeanDefinition对象
    private static final ConcurrentHashMap<String,AopBeanDefinition> aopBeanDefinitionMap = new ConcurrentHashMap<>();

    //定义ConcurrentHashMap变量aopBeanMap，用于存放被代理后的对象
    private static final ConcurrentHashMap<String,Object> aopBeanMap = new ConcurrentHashMap<>();

    /**
     * 根据bean的名称获取bean的实例
     * 如果该bean已经被代理，则返回代理后的bean
     * 如果该bean还没有被代理，则根据AopBeanDefinition创建代理
     * 如果该bean不需要被代理，则调用父类的getBean()方法获取bean的实例
     */
    @Override
    public Object getBean(String name) throws Exception {

        Object aopBean = aopBeanMap.get(name);

        if(aopBean != null){
            return aopBean;
        }

        if(aopBeanDefinitionMap.containsKey(name)){
            AopBeanDefinition aopBeanDefinition = aopBeanDefinitionMap.get(name);
            AdvisedSupport advisedSupport = getAdvisedSupport(aopBeanDefinition);
            aopBean = new CglibAopProxy(advisedSupport).getProxy();
            aopBeanMap.put(name,aopBean);
            return aopBean;
        }

        return super.getBean(name);
    }

    /**
     * 注册AopBeanDefinition对象
     */
    protected void registerBean(String name, AopBeanDefinition aopBeanDefinition){
        aopBeanDefinitionMap.put(name,aopBeanDefinition);
    }

    /**
     * 创建AdvisedSupport对象
     */
    private AdvisedSupport getAdvisedSupport(AopBeanDefinition aopBeanDefinition) throws Exception {

        AdvisedSupport advisedSupport = new AdvisedSupport();
        List<String> interceptorNames = aopBeanDefinition.getInterceptorNames();
        if(interceptorNames != null && !interceptorNames.isEmpty()){
            for (String interceptorName : interceptorNames) {

                Advice advice = (Advice) getBean(interceptorName);

                Advisor advisor = new Advisor();
                advisor.setAdvice(advice);

                if(advice instanceof BeforeMethodAdvice){
                    AopMethodInterceptor interceptor = BeforeMethodAdviceAdapter.getInstants().getInterceptor(advisor);
                    advisedSupport.addAopMethodInterceptor(interceptor);
                }

                if(advice instanceof AfterRunningAdvice){
                    AopMethodInterceptor interceptor = AfterRunningAdviceAdapter.getInstants().getInterceptor(advisor);
                    advisedSupport.addAopMethodInterceptor(interceptor);
                }

            }
        }

        TargetSource targetSource = new TargetSource();

        //获取目标对象
        Object object = getBean(aopBeanDefinition.getTarget());

        //设置目标对象的类
        targetSource.setTagetClass(object.getClass());
        targetSource.setTagetObject(object);
        advisedSupport.setTargetSource(targetSource);


        return advisedSupport;

    }

}

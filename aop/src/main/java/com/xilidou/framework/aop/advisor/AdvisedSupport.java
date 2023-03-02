package com.xilidou.framework.aop.advisor;

import com.xilidou.framework.aop.interceptor.AopMethodInterceptor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhengxin
 */

@Data
public class AdvisedSupport extends Advisor {

    //目标对象
    private TargetSource targetSource;

    //拦截器列表
    private List<AopMethodInterceptor> list = new LinkedList<>();

    //将一个拦截器（AopMethodInterceptor对象）添加到列表中
    public void addAopMethodInterceptor(AopMethodInterceptor interceptor){
        list.add(interceptor);
    }

    //将作为参数的列表（interceptors）中的一群拦截器都添加到列表（list）中
    public void addAopMethodInterceptors(List<AopMethodInterceptor> interceptors){
        list.addAll(interceptors);
    }

}

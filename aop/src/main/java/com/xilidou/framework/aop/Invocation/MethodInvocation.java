package com.xilidou.framework.aop.Invocation;

import java.lang.reflect.Method;

/**
 * 用于描述方法的调用
 * @author Zhengxin
 */

public interface MethodInvocation {

    //1. 获取方法本身
    Method getMethod();

    //2. 获取方法的参数
    Object[] getArguments();

    //3. 执行方法本身
    Object proceed() throws Throwable;

}

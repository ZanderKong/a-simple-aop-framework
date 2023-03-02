package com.xilidou.framework.aop.advisor;


import lombok.Data;

@Data
public class Advisor {

    //应该执行什么操作
    private Advice advice;
    //应该在什么时候执行这个操作
    private Pointcut pointcut;

}

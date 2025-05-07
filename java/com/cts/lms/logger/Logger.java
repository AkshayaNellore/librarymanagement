package com.cts.lms.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class Logger {

    @Pointcut(value = "execution(* com.cts.lms.controller.*.*(..))") // Adjusted pointcut to match your project structure
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object applicationLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName(); // Use getSimpleName() for cleaner logging
        String methodName = joinPoint.getSignature().getName();
        log.info("{}::{}: Entry", className, methodName);
        Object obj = joinPoint.proceed();
        log.info("{}::{}: Exit", className, methodName);
        return obj;
    }
}
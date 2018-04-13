package com.cupdata.sip.common.lang.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 使用 @Aspect注解将一个java类定义为切面类
 * 使用 @Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
 * 根据 需要在切入点不同位置的切入内容
 * 使用 @Before在切入点开始处切入内容
 * 使用 @After在切入点结尾处切入内容
 * 使用 @AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
 * 使用 @Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
 * 使用 @AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
 */

@Aspect
@Configuration
@Slf4j
public class WebLogAspect {
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    //所有public 任意返回值 sip下的所有子包下以Controller结尾 的所有方法，可以为任意参数
    @Pointcut("execution(public * com.cupdata.sip..*Controller.*(..))")
    public void logPointcut(){}

    @Before("logPointcut()")
    public void doBefore(JoinPoint point) {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("Request URL : " + request.getRequestURL().toString());
        log.info("http-method : " + request.getMethod());
        log.info("Request IP : " + request.getRemoteAddr());
        log.debug("class.method() : " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        log.debug("args[] : " + Arrays.toString(point.getArgs()));
    }
    @AfterReturning(returning = "ret", pointcut = "logPointcut()")
    public void doAfterReturning(Object ret){
        // 处理完请求，返回内容
        log.info("Response result : " + ret);
        log.debug("total time : " + (System.currentTimeMillis() - startTime.get()));
    }

}

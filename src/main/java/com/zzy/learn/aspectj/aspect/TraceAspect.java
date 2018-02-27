package com.zzy.learn.aspectj.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Package: com.zzy.learn.aspectj.aspect
 * Class: TraceAspect
 * Description: 方法拦截切面
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/18 9:49
 */
@Aspect
public class TraceAspect {
    private static final String POINT_METHOD = "execution(* com.example.app.LoginActivity.*(..))";
    private static final String POINT_CALLMETHOD = "call(* com.example.app.LoginActivity.*(..))";

    @Pointcut(POINT_METHOD)
    public void methodAnnotated() {
    }

    @Pointcut(POINT_CALLMETHOD)
    public void methodCallAnnotated() {
    }

    /**
     * Method: aroundJoinPoint
     * Description: 织入点
     * @param   joinPoint
     * @return  Object 原方法返回值
     * @exception/throws Throwable
     */
    @Around("methodAnnotated()")
    public Object aronudWeaverPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        final long endTime = System.currentTimeMillis();
        Log.d(className, buildLogMessage(methodName, endTime - startTime));
        return result;
    }
    /**
     * Method: aroundJoinPoint
     * Description: 织入点
     * @param   joinPoint
     * @return  void
     */
    @Before("methodCallAnnotated()")
    public void beforecall(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Log.e(className, String.format("before run %s", methodName));
    }
    /**
     * Method: buildLogMessage
     * Description: 构造日志信息
     * @param methodName 方法名
     * @param methodDuration 方法执行耗时时间 毫秒
     * @return  String
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        return String.format("%s run time:%d ms", methodName, methodDuration);
    }
}

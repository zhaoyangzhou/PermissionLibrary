
package com.zzy.learn.aspectj.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Package: com.zzy.learn.aspectj.aspect
 * Class: DebugTraceAspect
 * Description: 日志切面
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/18 9:49
 */
@Aspect
public class DebugTraceAspect {

    private static final String POINTCUT_METHOD =
            "execution(@com.zzy.learn.aspectj.annotation.DebugTrace * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.zzy.learn.aspectj.annotation.DebugTrace *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugTrace() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedDebugTrace() {
    }
    /**
     * Method: weaveJoinPoint
     * Description: 织入点
     * @param joinPoint
     * @return  原方法返回值
     * @exception/throws Throwable
     */
    @Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
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

package com.zzy.learn.aspectj.aspect;

import android.app.Activity;

import com.zzy.learn.aspectj.annotation.Permission;
import com.zzy.learn.aspectj.util.MPermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.ref.WeakReference;

/**
 * Package: com.zzy.learn.aspectj.aspect
 * Class: PermissionAspectj
 * Description:
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/18 11:00
 */
@Aspect
public abstract class PermissionAspectj {
    /**
     * Method: aroundJoinPoint
     * Description: 织入点，所有使用Permission注解的方法
     * @param   joinPoint
     * @param   permission
     * @return  void
     * @exception/throws [违例类型] [违例说明]
     */
    @Around("execution(@com.zzy.learn.aspectj.annotation.Permission * *(..)) && @annotation(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final Permission permission) throws Throwable {
        final Activity ac = getCurrentActivity().get();
        MPermissionUtil.requestPermissionsResult(ac, 1, permission.value()
                , new MPermissionUtil.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        try {
                            joinPoint.proceed();//获得权限，执行原方法
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtil.showTipsDialog(ac);
                    }
                });
    }
    /**
     * Method:  getCurrentActivity
     * Description: 获取当前显示的Activity引用
     * @return  WeakReference<Activity>
     */
    public abstract WeakReference<Activity> getCurrentActivity();
}

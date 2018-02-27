
# PermissionLibrary
使用AOP实现动态申请权限，只需简单几步即可集成。


# 集成步骤：

## 1、在工程的build.gradle文件中修改配置：
```
allprojects {

    repositories {
	
		...
		
		maven { url 'https://jitpack.io' }
		
	}
	
}
```
## 2、在app module的build.gradle文件中修改配置：
```
dependencies {

    compile 'com.github.zhaoyangzhou:PermissionLibrary:v1.0'
	
}
```

## 3、在app module的build.gradle文件末尾增加配置：
```
import org.aspectj.bridge.IMessage

import org.aspectj.bridge.MessageHandler

import org.aspectj.tools.ajc.Main


buildscript {

    repositories {
    
        mavenCentral()
    
    }
    
    dependencies {
    
        classpath 'org.aspectj:aspectjtools:1.8.9'
	
    }
    
}


repositories {

    mavenCentral()
    
}


final def log = project.logger

final def variants = project.android.applicationVariants


variants.all { variant ->

    if (!variant.buildType.isDebuggable()) {
    
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
	
        return;
	
    }
    

    JavaCompile javaCompile = variant.javaCompile
    
    javaCompile.doLast {
    
        String[] args = ["-showWeaveInfo",
	
                         "-1.5",
			 
                         "-inpath", javaCompile.destinationDir.toString(),
			 
                         "-aspectpath", javaCompile.classpath.asPath,
			 
                         "-d", javaCompile.destinationDir.toString(),
			 
                         "-classpath", javaCompile.classpath.asPath,
			 
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
			 
        log.debug "ajc args: " + Arrays.toString(args)
	

        MessageHandler handler = new MessageHandler(true);
	
        new Main().run(args, handler);
	
        for (IMessage message : handler.getMessages(null, true)) {
	
            switch (message.getKind()) {
	    
                case IMessage.ABORT:
		
                case IMessage.ERROR:
		
                case IMessage.FAIL:
		
                    log.error message.message, message.thrown
		    
                    break;
		    
                case IMessage.WARNING:
		
                    log.warn message.message, message.thrown
		    
                    break;
		    
                case IMessage.INFO:
		
                    log.info message.message, message.thrown
		    
                    break;
		    
                case IMessage.DEBUG:
		
                    log.debug message.message, message.thrown
		    
                    break;
		    
            }
	    
        }
	
    }
    
}
```

## 4、增加Application实现类：
```
public class AppApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static AppApplication CONTEXT;
    

    public static AppApplication getInstance() {
    
        return CONTEXT;
	
    }
    

    @Override
    
    public void onCreate() {
    
        super.onCreate();
	
        CONTEXT = this;
	
        registerActivityLifecycleCallbacks(this);
	
    }
    

    @Override
    
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    
    public void onActivityStarted(Activity activity) {}

    @Override
    
    public void onActivityResumed(Activity activity) {
    
        ActivityManager.getInstance().setCurrentActivity(activity);
	
    }

    @Override
    
    public void onActivityPaused(Activity activity) {}

    @Override
    
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
```
## 5、增加PermissionAspectj实现类：
```
@Aspect

public class PermissionAspectjImpl extends PermissionAspectj {

    /**
     * Method: getCurrentActivity
     * Description: 获取当前显示的Activity引用
     * @return  WeakReference<Activity>
     */
    @Override
    public WeakReference<Activity> getCurrentActivity() {
        return ActivityManager.getInstance().getCurrentActivity();
    }
}
```  
## 6、Activity中重写onRequestPermissionsResult方法，并在需要触发权限申请的方法前增加注解：
```
/**
 * Method: onRequestPermissionsResult
 * Description: 授权结果回调方法
 * @param requestCode 请求参数
 * @param permissions 权限列表
 * @param grantResults 授权结果
 * @return  void
 */
@Override

public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
    MPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
}


@Permission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE})

@DebugTrace

public void toNextView() {

    Toast.makeText(this, "申请权限 用户已授权", Toast.LENGTH_SHORT).show();
    
}
```
## 7、AndroidManifest.xml文件中增加需要申请的权限列表：
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

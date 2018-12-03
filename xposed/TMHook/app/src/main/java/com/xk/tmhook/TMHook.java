package com.xk.tmhook;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedBridge.hookMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class TMHook
{
    private static String class_name = "";
    private static LoadPackageParam loadPackageParam = null;

    static void init(final LoadPackageParam lpparam)
    {
        loadPackageParam = lpparam;
        hookFunc(loadPackageParam);
    }

    public static boolean filter_pkg_name(Method m)
    {
        if (    m.toString().contains("aweme" ) ||
                Modifier.isAbstract(m.getModifiers()) ||
                Modifier.isNative(m.getModifiers()) ||
                Modifier.isInterface(m.getModifiers()))
            return true;
        else
            return false;
    }

    public static void hookFunc(final LoadPackageParam lpparam)
    {
        findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                if (param.hasThrowable())
                    return ;

                synchronized (this.getClass())
                {
                    // 获取loadClass返回类对象
                    Class<?> clazz = (Class<?>) param.getResult();

                    // 保存类对象
                    class_name = clazz.getName();

                    Method[] m = clazz.getDeclaredMethods();
                    for (int i = 0; i < m.length; i++)
                    {
                        // 过滤标准库和第三方库
                        if(filter_pkg_name(m[i]))
                        {
                            hookMethod(m[i], new XC_MethodHook()
                            {
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                                {
                                    String method_name = param.method.getName();
                                    log("   >>> " + class_name + "-" + method_name);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
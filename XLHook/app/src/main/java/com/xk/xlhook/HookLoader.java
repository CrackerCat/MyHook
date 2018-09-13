package com.xk.xlhook;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import java.lang.reflect.Method;
import dalvik.system.PathClassLoader;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookLoader implements IXposedHookLoadPackage 
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam)
    {
        if (!lpparam.packageName.equals("com.ss.android.ugc.live"))
            return;

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                Context ctx = (Context) param.args[0];
                lpparam.classLoader = ctx.getClassLoader();
                PathClassLoader cl = new PathClassLoader(findApkFile(ctx, "com.xk.xlhook"), ClassLoader.getSystemClassLoader());
                Class<?> cls = Class.forName("com.xk.xlhook.MainHook", true, cl);
                Object instance = cls.newInstance();
                Method method = cls.getDeclaredMethod("handleLoadPackage", XC_LoadPackage.LoadPackageParam.class);
                method.invoke(instance, lpparam);
            }
        });
    }

    private String findApkFile(Context context, String pkgName)
    {
        try {
            Context ctx = context.createPackageContext(pkgName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            return ctx.getPackageCodePath();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
package com.xk.fvhook;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import de.robv.android.xposed.XC_MethodHook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable
    {
        if (!loadPackageParam.packageName.equals("com.ss.android.ugc.aweme"))
            return;

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                Class<?> clazz = findClass("com.ss.android.ugc.aweme.splash.SplashActivity", loadPackageParam.classLoader);
                findAndHookMethod(clazz, "onCreate", Bundle.class, new XC_MethodHook()
                {
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable
                    {
                        Thread thread = new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    log("Start TMHook ~~~");
                                    Thread.sleep(3000L);
                                    FVHook.init(loadPackageParam);

                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                    log("Start error ~~~");
                                }
                            }
                        });
                        thread.start();
                    }
                });
            }
        });

    }
}

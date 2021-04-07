package org.xk.xpexample;

import android.util.Log;
import com.xiaojianbang.app.XC_MethodHook;
import com.xiaojianbang.app.XposedHelpers;
import com.xiaojianbang.app.IXposedHookLoadPackage;
import com.xiaojianbang.app.callbacks.XC_LoadPackage;

import java.util.Map;


public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        Log.d("XK", "hooking...");

        if (!lpparam.packageName.equals("com.smile.gifmaker")) return;

        XposedHelpers.findAndHookMethod(
                "com.yxcorp.gifshow.retrofit.c",
                lpparam.classLoader,
                "a",
                Map.class, Map.class,
                new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        Log.d("XK","result: " + param.getResult());
                    }
                });
    }
}

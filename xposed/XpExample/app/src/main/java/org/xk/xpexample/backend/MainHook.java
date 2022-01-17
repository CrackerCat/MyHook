package org.xk.xpexample.backend;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        if (!lpparam.packageName.equals("com.dianping.v1"))
            return;

        DianPingHook dp = new DianPingHook();
        dp.HookNetMoudle(lpparam);
    }
}
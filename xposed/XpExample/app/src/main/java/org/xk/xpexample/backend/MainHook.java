package org.xk.xpexample.backend;

import com.elvishew.xlog.XLog;
import com.elvishew.xlog.LogLevel;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        XLog.init(LogLevel.ALL);

        if (lpparam.packageName.equals("com.dianping.v1"))
        {
            DianPingHook DianPing = new DianPingHook();
            DianPing.hook_decryptData(lpparam.classLoader);
        }

        if (lpparam.packageName.equals("com.sankuai.meituan"))
        {
            MeituanHook Meitun = new MeituanHook();
            Meitun.tcp_downgrade_http(lpparam.classLoader);
            Meitun.monitor_okttp(lpparam.classLoader);
        }
    }
}
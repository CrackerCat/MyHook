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

        // 大众点评
        if (lpparam.packageName.equals("com.dianping.v1"))
        {
            DianPingHook DianPing = new DianPingHook();
            DianPing.hook_g_dec(lpparam.classLoader);
            DianPing.hook_a_aes(lpparam.classLoader);
            DianPing.hook_toString(lpparam.classLoader);
            DianPing.hook_decryptData(lpparam.classLoader);
        }

        // 美团
        if (lpparam.packageName.equals("com.sankuai.meituan"))
        {
            MeituanHook Meitun = new MeituanHook();
            Meitun.tcp_downgrade_http(lpparam.classLoader);
            Meitun.monitor_okttp(lpparam.classLoader);
        }

        // 美团外卖
        if(lpparam.packageName.equals("com.sankuai.meituan.takeoutnew"))
        {
            MeituanWaiMaiHook Waimai = new MeituanWaiMaiHook();
            Waimai.hook_getRequestSignature(lpparam.classLoader);
            Waimai.anti_env_check(lpparam.classLoader);
            Waimai.hook_siua(lpparam.classLoader);
            Waimai.hook_collection(lpparam.classLoader);
            Waimai.hook_encrypt(lpparam.classLoader);
            Waimai.hook_decrypt(lpparam.classLoader);
        }
    }
}
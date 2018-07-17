package com.xk.xkhook;

import java.util.Map;
import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable
    {
        if (!loadPackageParam.packageName.equals("com.smile.gifmaker"))
            return;

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                ClassLoader cl = ((Context)param.args[0]).getClassLoader();
                Class<?> tx_clazz = cl.loadClass("com.tencent.bugly.crashreport.common.info.b");

                XposedHelpers.findAndHookMethod(tx_clazz,"m", Context.class, new XC_MethodHook()
                {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable
                    {
                        param.setResult(false);
                    }
                });

//                Map m1 = new HashMap(); m1.put("111", "aaa");
//                Map m2 = new HashMap(); m2.put("222", "bbb");
//                final Object cpu_obj = XposedHelpers.newInstance(cup_clazz);
//                XposedHelpers.callMethod(cpu_obj, "a", m1, m2);

                Class<?> gif_clazz = XposedHelpers.findClass("com.yxcorp.gifshow.retrofit.c", loadPackageParam.classLoader);
                XposedHelpers.findAndHookMethod(gif_clazz, "a", Map.class, Map.class, new XC_MethodHook()
                {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable
                    {
                        XposedBridge.log("> GIF_CLAZZ:\r\n");

                        for (int i = 0; i < param.args.length; i++)
                        {
                            if(param.args[i].getClass().getSimpleName().equals("byte[]"))
                            {
                                XposedBridge.log("> arg_" + i + "_hex:" + HexDumper.toHexString((byte[])param.args[i]));
                                XposedBridge.log("> arg_" + i + "_byte:\r\n" + HexDumper.dumpHexString((byte[])param.args[i]));
                            }
                            else
                                XposedBridge.log("> arg_" + i + "_str:" + param.args[i].toString());
                        }
                    }
                });

                Class<?> cpu_clazz = XposedHelpers.findClass("com.yxcorp.gifshow.util.CPU", loadPackageParam.classLoader);
                XposedHelpers.findAndHookMethod(cpu_clazz, "a", Context.class, byte[].class, int.class, new XC_MethodHook()
                {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable
                    {
                        XposedBridge.log("> CUP_CLAZZ:\r\n");

                        for (int i = 0; i < param.args.length; i++)
                        {
                            if(param.args[i].getClass().getSimpleName().equals("byte[]"))
                            {
                                XposedBridge.log("> arg_" + i + "_hex:" + HexDumper.toHexString((byte[])param.args[i]));
                                XposedBridge.log("> arg_" + i + "_byte:\r\n" + HexDumper.dumpHexString((byte[])param.args[i]));
                            }
                            else
                                XposedBridge.log("> arg_" + i + "_str:" + param.args[i].toString());
                        }

                        if(param.getResult().getClass().getSimpleName().equals("byte[]"))
                        {
                            XposedBridge.log("> ret_hex:" + HexDumper.toHexString((byte[])param.getResult()));
                            XposedBridge.log("> ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
                        }
                        else
                            XposedBridge.log("> ret_str:" + param.getResult().toString());
                    }
                });
            }
        });
    }

}

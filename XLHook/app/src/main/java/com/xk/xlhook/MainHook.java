package com.xk.xlhook;

import java.util.List;
import android.content.Context;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam)
    {
        XposedHelpers.findAndHookMethod("com.ss.sys.ces.b.a", lpparam.classLoader, "a", Context.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                param.setResult(false);
            }
        });

        Class<?> clazz = XposedHelpers.findClass("com.ss.android.ugc.antispam.l", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz,"a", String.class, List.class, boolean.class, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable
            {
                for (int i = 0; i < param.args.length; i++)
                {
                    if(param.args[i].getClass().getSimpleName().equals("byte[]"))
                    {
                        XposedBridge.log("> arg_" + i + "_hex:" + HexDumper.toHexString((byte[])param.args[i]));
                        XposedBridge.log("> arg_" + i + "_byte:\r\n" + HexDumper.dumpHexString((byte[])param.args[i]));
                    }
                    else if (param.args[i].getClass().getSimpleName().equals("String[]"))
                    {
                        StringBuffer stringBuffer = new StringBuffer();
                        String[] strings = (String[]) param.args[i];

                        for (int x = 0; x < strings.length; x++)
                            stringBuffer.append("\"" + strings[x] + "\", ");

                        XposedBridge.log("> arg_" + i + "_str_arry:" + stringBuffer.toString());
                    }
                    else
                        XposedBridge.log("> arg_" + i + "_str:" + param.args[i].toString());
                }
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                if(param.getResult().getClass().getSimpleName().equals("byte[]"))
                {
                    XposedBridge.log("> ret_hex:" + HexDumper.toHexString((byte[])param.getResult()));
                    XposedBridge.log("> ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
                }
                else
                    XposedBridge.log("> ret_str:" + param.getResult().toString());
            }
        });

        Class<?> user_info_obj = XposedHelpers.findClass("com.ss.android.common.applog.UserInfo", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(user_info_obj,"getUserInfo", int.class, String.class, String[].class, String.class, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable
            {
                for (int i = 0; i < param.args.length; i++)
                {
                    if(param.args[i].getClass().getSimpleName().equals("byte[]"))
                    {
                        XposedBridge.log("> arg_" + i + "_hex:" + HexDumper.toHexString((byte[])param.args[i]));
                        XposedBridge.log("> arg_" + i + "_byte:\r\n" + HexDumper.dumpHexString((byte[])param.args[i]));
                    }
                    else if (param.args[i].getClass().getSimpleName().equals("String[]"))
                    {
                        StringBuffer stringBuffer = new StringBuffer();
                        String[] strings = (String[]) param.args[i];

                        for (int x = 0; x < strings.length; x++)
                            stringBuffer.append("\"" + strings[x] + "\", ");

                        XposedBridge.log("> arg_" + i + "_str_arry:" + stringBuffer.toString());
                    }
                    else
                        XposedBridge.log("> arg_" + i + "_str:" + param.args[i].toString());
                }
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
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
}

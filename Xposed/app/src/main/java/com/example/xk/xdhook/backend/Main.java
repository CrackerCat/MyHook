package com.example.xk.xdhook.backend;

import com.example.xk.xdhook.util.ArgsInfo;
import com.example.xk.xdhook.util.HexDumper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.IXposedHookLoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookLoadPackage
{
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        ArgsInfo args = new ArgsInfo();
        if (!lpparam.packageName.equals(args.package_name))
            return;

        XposedBridge.log("class:" + args.class_name);
        XposedBridge.log("funtion:" +args.function_name);

        Class<?> clazz = XposedHelpers.findClass(args.class_name, lpparam.classLoader);
        XposedBridge.hookAllMethods(clazz, args.function_name, new XC_MethodHook()
        {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable
            {
                XposedBridge.log("> hookAllMethods start !!!");

                super.beforeHookedMethod(param);
                for (int i = 0; i < param.args.length; i++)
                {
                    if(param.args[i].getClass().getSimpleName() == "byte[]")
                    {
                        XposedBridge.log("[arg" + i + "]:\r\n" + param.args[i].toString());
                        XposedBridge.log("[arg" + i + "]:\r\n" + HexDumper.toHexString(param.args[i].toString().getBytes()));
                        XposedBridge.log("[arg" + i + "]:\r\n" + HexDumper.dumpHexString(param.args[i].toString().getBytes()));
                        continue;
                    }
                    XposedBridge.log("[arg" + i + "]:\r\n" + param.args[i].toString());
                }

                RuntimeException e = new RuntimeException("run is here");
                e.fillInStackTrace();
                XposedBridge.log("> StackTrace:\r\n" + e);
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                super.afterHookedMethod(param);
                XposedBridge.log("[Ret]:" + param.getResult());
            }
        });

//        findAndHookMethod(args.class_name, lpparam.classLoader, args.function_name, args.args_obj, new XC_MethodHook()
//        {
//            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
//            {
//                for (int i = 0; i < param.args.length; i++)
//                {
//                    if(param.args[i].getClass().getSimpleName() == "byte[]")
//                    {
//                        XposedBridge.log("[arg" + i + "]:\r\n" + param.args[i].toString());
//                        XposedBridge.log("[arg" + i + "]:\r\n" + HexDumper.toHexString(param.args[i].toString().getBytes()));
//                        XposedBridge.log("[arg" + i + "]:\r\n" + HexDumper.dumpHexString(param.args[i].toString().getBytes()));
//                        continue;
//                    }
//                    XposedBridge.log("[arg" + i + "]:\r\n" + param.args[i].toString());
//                }
//
//                RuntimeException e = new RuntimeException("run is here");
//                e.fillInStackTrace();
//                XposedBridge.log("> StackTrace:\r\n" + e);
//            }
//
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
//            {
//                XposedBridge.log("[Ret]:" + param.getResult());
//            }
//        });
    }
}






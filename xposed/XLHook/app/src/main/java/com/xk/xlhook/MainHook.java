package com.xk.xlhook;

import android.content.Context;

import java.util.Map;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage
{
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam)
    {
         // hook框架检测
        Class<?> a_obj = XposedHelpers.findClass("com.tencent.bugly.crashreport.common.info.a", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(a_obj, "S", new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                param.setResult(false);
            }
        });

        Class<?> b_obj = XposedHelpers.findClass("com.tencent.bugly.crashreport.common.info.b", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(b_obj, "p", Context.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                param.setResult(false);
            }
        });


        /*
            public Response intercept(Chain arg4) throws IOException
            {
                Builder v0 = arg4.request().newBuilder();
                String v1 = this.b.a();

                if(!TextUtils.isEmpty(((CharSequence)v1)))
                    v0.header("Authorization", v1);
                v0.header("User-Agent", this.a());
                v0.header("device_id", AppInfoUtils.b());  // hook成功

                this.a(v0, arg4);                          // 发现hook失败
                return this.a(arg4, v0, arg4.proceed(v0.build()));
            }
        */

        // test anti hook
        Class<?> chain_obj = XposedHelpers.findClass("okhttp3.Interceptor$Chain", lpparam.classLoader);
        Class<?> request_obj = XposedHelpers.findClass("okhttp3.Request", lpparam.classLoader);
        Class<?> builder_obj = XposedHelpers.findClass("okhttp3.Request$Builder", lpparam.classLoader);
        Class<?> add_hed_obj = XposedHelpers.findClass("com.xingin.skynet.AddCommonHeaderInterceptor", lpparam.classLoader);

        // intercept(Chain arg4)
        XposedHelpers.findAndHookMethod(add_hed_obj, "intercept", chain_obj, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log(">>> intercept(Chain arg4) hook sucess ~");
            }
        });

        // v0.header()
        XposedHelpers.findAndHookMethod(builder_obj, "header", String.class, String.class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String key = param.args[0].toString();
                String val = param.args[1].toString();
                if(key.equals("shield"))
                    XposedBridge.log(">v0.header(" + key + ", " + val + ")");
            }
        });


        // this.a();
        XposedHelpers.findAndHookMethod(add_hed_obj, "a", builder_obj, chain_obj, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("void a(Builder arg3, Chain arg4) hook sucess~");
            }
        });

        // enc_1
        XposedHelpers.findAndHookMethod(add_hed_obj, "a", request_obj, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("a_ret:" + param.getResult().toString());
                RuntimeException e = new RuntimeException(">>> Run is here");
                e.fillInStackTrace();
                XposedBridge.log("> StackTrace:\\r\\n" + e);
            }
        });

        // enc_2
        XposedHelpers.findAndHookMethod(add_hed_obj,"a", Map.class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("> map_arg:" + param.args[0].toString());
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("> ret_hex:" + HexDumper.toHexString((byte[])param.getResult()));
                XposedBridge.log("> ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
            }
        });

        // enc_3
        XposedHelpers.findAndHookMethod(add_hed_obj,"a", byte[].class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("> arg_hex:" + HexDumper.toHexString((byte[])param.args[0]));
                XposedBridge.log("> arg_byte:\r\n" + HexDumper.dumpHexString((byte[])param.args[0]));
            }

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("> shield:" + param.getResult());
            }
        });
    }
}

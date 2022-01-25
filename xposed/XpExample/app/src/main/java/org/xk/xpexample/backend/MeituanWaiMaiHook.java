package org.xk.xpexample.backend;

import com.elvishew.xlog.XLog;

import org.xk.xpexample.utils.HexDumper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

class MeituanWaiMaiHook
{
    // 环境检测
    public void anti_env_check(final ClassLoader classLoader)
    {
        // boolean inSandBox()
        // boolean isAccessibilityEnable()
        // boolean isCameraHack()
        // boolean isDarkSystem()
        // boolean isDebug()
        // boolean isEmu()
        // boolean isHook()
        // boolean isProxy()
        // boolean isRemoteCall()
        // boolean isRoot()
        // boolean isSigCheckOK()
        // boolean isVirtualLocation()
        // boolean isproxyDetect()
        // boolean isrootDetect()
        // boolean issimulatorDetect()
        try
        {
            String class_name = "com.meituan.android.common.mtguard.MTGuard";
            XposedHelpers.findAndHookMethod(class_name, classLoader, "inSandBox", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isAccessibilityEnable", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isCameraHack", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isDarkSystem", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isDebug", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isEmu", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isHook", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isProxy", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isRemoteCall", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isRoot", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isSigCheckOK", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isVirtualLocation", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isproxyDetect", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "isrootDetect", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });

            XposedHelpers.findAndHookMethod(class_name, classLoader, "issimulatorDetect", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });
            XLog.d("anti_env_check hook sucended!");
        }
        catch(Throwable e)
        {
            XLog.d("anti_env_check" + e.getMessage());
            e.printStackTrace();
        }
    }

    // byte[] siua()
    public void hook_siua(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.meituan.android.common.mtguard.MTGuard";
            String function_name = "siua";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    XLog.d("ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
                    printStackTrace();
                }
            });
            XLog.d("hook_siua succended!");
        }
        catch(Throwable e)
        {
            XLog.d("hook_siua error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    // String encrypt(String str, String str2)
    public void hook_encrypt(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.meituan.android.common.unionid.oneid.util.DESHelper";
            String function_name = "encrypt";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, String.class, String.class, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    XLog.d("arg_1_str" + param.args[0]);
                    XLog.d("arg_2_str" + param.args[1]);
                    XLog.d("ret_str:" + param.getResult().toString());
                }
            });
            XLog.d("hook_encrypt succended!");
        }
        catch(Throwable e)
        {
            XLog.d("hook_encrypt error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    // String decrypt(String str, String str2)
    public void hook_decrypt(final ClassLoader classLoader)
    {
        try{
            String class_name = "com.meituan.android.common.unionid.oneid.util.DESHelper";
            String function_name = "decrypt";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, String.class, String.class, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.beforeHookedMethod(param);
                    XLog.d("args_1_str:" + param.args[0]);
                    XLog.d("args_2_str:" + param.args[1]);
                    XLog.d("ret_str:" + param.getResult().toString());
                }
            });
            XLog.d("hook_decrypt succeeded!");
        }
        catch (Throwable e)
        {
            XLog.d("hook_decrypt error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printStackTrace()
    {
        XLog.d("------------------------------>");
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        for (int i = 0; i < stackElements.length; i++)
        {
            StackTraceElement element = stackElements[i];
            XLog.d("\t" + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
        }
    }
}

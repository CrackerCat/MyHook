package org.xk.xpexample.backend;

import com.elvishew.xlog.XLog;

import org.xk.xpexample.utils.HexDumper;

import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

class MeituanWaiMaiHook
{
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
                    XLog.d("hook_siua succended!");
                }
            });
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
                    XLog.d("hook_encrypt succended!");
                }
            });
        }
        catch(Throwable e)
        {
            XLog.d("hook_encrypt error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    // byte[] encryptByte(byte[] bArr, Key key, DESKeySpec dESKeySpec)
    public void hook_encryptByte(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.meituan.android.common.unionid.oneid.util.DESHelper";
            String function_name = "encryptByte";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, byte[].class, SecretKey.class, DESKeySpec.class, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    XLog.d("arg_1_byte:\r\n" + HexDumper.dumpHexString((byte[])param.args[0]));
                    XLog.d("ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
                    XLog.d("encryptByte succended!");
                }
            });
        }
        catch(Throwable e)
        {
            XLog.d("encryptByte error!" + e.getMessage());
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
}

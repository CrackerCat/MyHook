package org.xk.xpexample.backend;

import com.elvishew.xlog.XLog;

import org.xk.xpexample.utils.HexDumper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

class DianPingHook
{
    // 此处有一次返回String 碰碰运气
    public void hook_toString(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.dianping.archive.e";
            String function_name = "toString";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    XLog.d("toString_ret:" + param.getResult().toString());
                }
            });
            XLog.d("hook_toString succeeded!");
        }
        catch(Throwable e)
        {
            XLog.d("hook_toString error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 此处返回String 观察一下 是否时我想要的数据
    public void hook_g_dec(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.dianping.archive.e";
            String function_name = "g";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    XLog.d("g_ret:" + param.getResult().toString());
                }
            });
            XLog.d("hook_g_dec succeeded!");
        }
        catch (Throwable e)
        {
            XLog.d("hook_g_dec error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 传入参数为Base64解密后数据 返回为AES加密后数据
    // 这里可以看到AES解密后 根据第一个字节判断使用那一种算法解密
    // 发现这个函数在调用评论留言接口时候似乎没有走 有待进一步测试 如果屎那就嗝屁了
    public void hook_a_aes(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.dianping.dataservice.mapi.impl.e";
            String function_name = "a";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, byte[].class, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.beforeHookedMethod(param);
                    XLog.d("byte[] a_aes(byte[])");

                    XLog.d("> arg_1_byte:\n" + HexDumper.dumpHexString((byte[])param.args[0]));
                    XLog.d("> arg_1_hex:" + HexDumper.toHexString((byte[])param.args[0]));

                    XLog.d("> ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
                    XLog.d("> ret_str:" + new String((byte[])param.getResult(), "UTF-8"));
                }
            });
            XLog.d("hook_a_aes succeeded!");
        }
        catch(Throwable e)
        {
            XLog.d("hook_a_aes error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 测试过了不是这个B函数
    public void hook_decryptData(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.dianping.nvnetwork.tunnel.Encrypt.SocketSecureManager";
            String function_name = "decryptData";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, byte[].class, String.class, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.beforeHookedMethod(param);
                    XLog.d("decryptData(byte[], String)");
                    XLog.d("> arg_1_byte:\n" + HexDumper.dumpHexString((byte[])param.args[0]));
                    XLog.d("> arg_1_hex:" + HexDumper.toHexString((byte[])param.args[0]));
                    XLog.d("> arg_2_str:" + param.args[1].toString());
                    XLog.d("> ret_byte:\r\n" + HexDumper.dumpHexString((byte[])param.getResult()));
                    XLog.d("> ret_hex:" + HexDumper.toHexString((byte[])param.getResult()));
                }
            });
            XLog.d("hook_decryptData succeeded!");
        }
        catch(Throwable e)
        {
            XLog.d("hook_decryptData error!" + e.getMessage());
            e.printStackTrace();
        }
    }
}

package org.xk.xpexample.backend;

import com.elvishew.xlog.XLog;

import org.xk.xpexample.utils.HexDumper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

class DianPingHook
{
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

package org.demo.xk.xposed;

import android.content.pm.ApplicationInfo;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


public class Main implements IXposedHookLoadPackage
{
    Class string_to_class(String type)
    {
        switch(type)
        {
            case "byte":    return byte.class;
            case "short":   return short.class;
            case "int":     return int.class;
            case "long":    return long.class;
            case "float":   return float.class;
            case "double":  return double.class;
            case "String":  return String.class;
            case "Map":     return Map.class;
            case "List":    return List.class;
            case "boolean": return boolean.class;
            case "char":    return char.class;

            default:        return null;
        }
    }

    XC_MethodHook callback_fun = new XC_MethodHook()
    {
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable
        {
            XposedBridge.log("[*] target hook successfully.....");

            for (int i = 0; i < param.args.length; i++)
                XposedBridge.log("\r\n> arg[" + i + "]:\r\n" + param.args[i]);

            Exception e = new Exception("this is a log");
            e.printStackTrace();
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable
        {

            String dbgstr = "";
            dbgstr += "> arg1:\r\n" + param.args[0].toString() + "\r\n";
            dbgstr += "> arg2:\r\n" + param.args[1].toString() + "\r\n";

            dbgstr += "\r\n> Hex: \r\n";
            dbgstr += HexDumper.toHexString(param.args[1].toString().getBytes()) + "\r\n";

            dbgstr += "\r\n> Dump: \r\n";
            dbgstr += HexDumper.dumpHexString(param.args[1].toString().getBytes()) + "\r\n";
            dbgstr += "\r\n";

            HexDumper.string2file("//sdcard//log.txt", dbgstr);

            XposedBridge.log(dbgstr);
            // XposedBridge.log("> Ret : \r\n" + param.getResult());
        }
    };

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        if(lpparam.appInfo == null || (lpparam.appInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0)
            return;

        // 配置文件路径
        String path = "/sdcard/config.txt";
        String package_name = null;     // 包名
        String class_name = null;       // 类名
        String function_name = null;    // 函数名
        String args_type = null;        // 参数类型

        Object[] args_obj = null;

        // 读取配置文件
        File file = new File(path);

        if (file.isDirectory())
            Log.d("XK", "File [/sdcard/config.txt] not exist");
        else
        {
            try
            {
                InputStream instream = new FileInputStream(file);

                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);

                    String line;

                    while (( line = buffreader.readLine()) != null)
                    {
                        if(line.indexOf("package_name") != -1)
                            package_name = line.substring(line.indexOf(":") + 1);
                        else if(line.indexOf("class_name") != -1)
                            class_name = line.substring(line.indexOf(":") + 1);
                        else if(line.indexOf("function_name") != -1)
                            function_name = line.substring(line.indexOf(":") + 1);
                        else if(line.indexOf("args_type") != -1)
                            args_type = line.substring(line.indexOf(":") + 1);
                    }

                    instream.close();
                }
            }
            catch (Exception e)
            {
                Log.d("XK", e.getMessage());
            }
        }


        // 解析参数
        Log.d("XK", "args resolve ~");

        int pos = 0;

        if((args_type != null) && (args_type.length() != 0))
        {
            String args_array[] = args_type.split(",");
            args_obj = new Object[args_array.length+1];

            // 参数转对象
            for (String tmp:args_array)
            {
                if(string_to_class(tmp) != null)
                    args_obj[pos] = string_to_class(tmp);
                else
                    args_obj[pos] = lpparam.classLoader.loadClass(tmp);
                pos++;
            }
        }
        else
        {
            args_obj = new Object[1];
        }

        args_obj[pos] = callback_fun;

        Log.d("XK", "package filtrate ~");
        if (!lpparam.packageName.equals(package_name))
            return;

        XposedBridge.log("[*] args resolve successfully.....");

        Log.e("XK", "package_name:" + package_name);
        Log.e("XK", "class_name:" + class_name);
        Log.e("XK", "function_name:" + function_name);
        Log.e("XK", "args_type:" + args_type);


        // 查找hook方法
        findAndHookMethod(class_name, lpparam.classLoader, function_name, args_obj);
    }
}
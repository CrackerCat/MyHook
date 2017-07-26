package org.demo.xk.xposed;

import java.util.Map;
import android.util.Log;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookLoadPackage
{
    private String dbgstr = "";
    private static final String TAG = "XK";
    private static final String LOG = "//data//local//tmp//log.txt";
    private static final String CONFIG = "//data//local//tmp//config.txt";

    public Class string_to_class(String type)
    {
        switch(type)
        {
            case "int":     return int.class;
            case "short":   return short.class;
            case "long":    return long.class;
            case "float":   return float.class;
            case "double":  return double.class;

            case "byte":    return byte.class;
            case "byte[]":  return byte[].class;

            case "Map":     return Map.class;
            case "char":    return char.class;
            case "String":  return String.class;
            case "boolean": return boolean.class;
            case "String[]":return String[].class;

            case "Context": return Context.class;
            default:        return null;
        }
    }

    public XC_MethodHook callback_fun = new XC_MethodHook()
    {
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable
        {
            for (int i = 0; i < param.args.length; i++)
                dbgstr += "\r\n> arg" + i + ":\r\n" + param.args[i].toString() + "\r\n";

            // dbgstr += "> arg1:\r\n" + param.args[0].toString() + "\r\n";
            // dbgstr += "> arg2:\r\n" + HexDumper.toHexString(param.args[1].toString().getBytes()) + "\r\n";

            RuntimeException e = new RuntimeException("run is here");
            e.fillInStackTrace();
            Log.d(TAG, "\r\n> StackTrace:", e);
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable
        {
            dbgstr += "\r\n> Ret:\r\n";
            dbgstr += param.getResult() + "\r\n";

            dbgstr += "\r\n> Hex:\r\n";
            dbgstr += HexDumper.toHexString(param.args[0].toString().getBytes()) + "\r\n";

            dbgstr += "\r\n> Dump:";
            dbgstr += HexDumper.dumpHexString(param.args[0].toString().getBytes()) + "\r\n";

            Log.d(TAG, dbgstr);
            HexDumper.string2file(LOG, dbgstr);
        }
    };

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        if(lpparam.appInfo == null || (lpparam.appInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0)
            return;

        String package_name = null;
        String class_name = null;
        String function_name = null;
        String args_type = null;

        Object[] args_obj = null;

        File file = new File(CONFIG);

        if (file.isDirectory())
        {
            Log.d(TAG, "config.txt open failed!");
        }
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
                Log.d(TAG, e.getMessage());
            }
        }

        int pos = 0;
        if((args_type != null) && (args_type.length() != 0))
        {
            String args_array[] = args_type.split(",");
            args_obj = new Object[args_array.length + 1];

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

        Log.d(TAG, "````````````````````````````````````````````````````````````````````````````````");
        Log.d(TAG, "package_name:" + package_name);
        Log.d(TAG, "class_name:" + class_name);
        Log.d(TAG, "function_name:" + function_name);
        Log.d(TAG, "args_type:" + args_type);

        if (!lpparam.packageName.equals(package_name))
            return;

        findAndHookMethod(class_name, lpparam.classLoader, function_name, args_obj);
    }
}






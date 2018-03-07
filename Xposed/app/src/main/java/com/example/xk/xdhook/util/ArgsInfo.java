package com.example.xk.xdhook.util;

import java.util.Map;
import android.os.Bundle;
import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import de.robv.android.xposed.XposedBridge;

public class ArgsInfo
{
    public ArgsInfo()
    {
        init_config_args();
        config_args_log();
    }

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
            case "String[]":return String[].class;

            case "boolean": return boolean.class;
            case "Bundle":  return Bundle.class;
            case "Context": return Context.class;
            default:        return null;
        }
    }

    public void config_args_log()
    {
        XposedBridge.log("````````````````````````````````````````````````````````````````````````````````");
        XposedBridge.log("package_name:" + package_name);
        XposedBridge.log("class_name:" + class_name);
        XposedBridge.log("function_name:" + function_name);
        XposedBridge.log("args_type:" + args_type);
    }

    public void init_config_args()
    {
        File file = new File(CONFIG_PATH);
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
        catch (Exception e) {
            XposedBridge.log(e.getMessage());
        }

        if((args_type != null) && (args_type.length() != 0))
        {
            String args_array[] = args_type.split(",");
            args_obj = new Object[args_array.length + 1];

            int pos = 0;
            for (String tmp:args_array)
            {
                if(string_to_class(tmp) != null)
                    args_obj[pos] = string_to_class(tmp);
                pos++;
            }
        }
        else
        {
            args_obj = new Object[1];
        }
    }

    public String package_name = null;
    public String class_name = null;
    public String function_name = null;
    public String args_type = null;
    public Object[] args_obj = null;
    public String CONFIG_PATH = "//data//local//tmp//config.txt";
}
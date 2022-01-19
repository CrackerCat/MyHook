package org.xk.xpexample.backend;

import com.elvishew.xlog.XLog;
import org.xk.xpexample.utils.Reflect;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

class MeituanHook
{
    public void tcp_downgrade_http(final ClassLoader classLoader)
    {
        try{
            String class_name = "com.sankuai.meituan.kernel.netimpl.INetFactoryImpl";
            String function_name = "a";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, String.class, new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.beforeHookedMethod(param);
                    param.args[0] = "okhttp";
                }
            });
            XLog.d("tcp_downgrade_http succeeded!");
        }
        catch (Throwable e)
        {
            XLog.d("tcp_downgrade_http error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void monitor_okttp(final ClassLoader classLoader)
    {
        try
        {
            String class_name = "com.sankuai.meituan.retrofit2.ClientCall";
            String function_name = "getResponseWithInterceptorChain";
            String parameterTypes = "com.sankuai.meituan.retrofit2.Request";
            XposedHelpers.findAndHookMethod(class_name, classLoader, function_name, parameterTypes, new XC_MethodHook()
            {
                @Override
                protected synchronized void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    super.afterHookedMethod(param);
                    if (param.getResult() == null)
                        return;

                    // 请求部分 com.sankuai.meituan.retrofit2.Request
                    Object request = param.args[0];
                    String url = (String) Reflect.on(request).call("url").get();        // String url() { return this.url; }
                    String method = (String) Reflect.on(request).call("method").get();  // String method() { return this.method; }
                    Object requestBody = Reflect.on(request).call("body").get();        // RequestBody body() { return this.body; }

                    XLog.d("````````````````````````````````````````````````````````````````````````````````");
                    XLog.d("请求链接:" + method + " " + url);

                    if (requestBody != null)
                    {
                        String contentType = Reflect.on(requestBody).call("contentType").get(); // String contentType();
                        if (contentType != null && contentType.equals("application/x-wwww-form-urlencoded"))
                        {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            Reflect.on(requestBody).call("writeTo", outputStream);  // void writeTo(OutputStream outputStream)
                            XLog.d("请求数据:" + outputStream.toString());
                            outputStream.close();
                        }
                    }

                    if (!param.getResult().getClass().getName().equals("com.sankuai.meituan.retrofit2.RawResponseSubject"))
                    {
                        XLog.d("未支持的网络请求:" + param.getResult().getClass().getName());
                        return;
                    }

                    // 响应部分 com.sankuai.meituan.retrofit2.ResponseBodySubject
                    Object ResponseBody = Reflect.on(param.getResult()).call("body").get();     // public ResponseBody body()
                    if (ResponseBody.getClass().getName().equals("com.sankuai.meituan.retrofit2.ResponseBodySubject"))
                    {
                        InputStream is = Reflect.on(ResponseBody).call("source").get();         // abstract InputStream source()
                        byte[] resultBytes = convertStreamToString(is);

                        XLog.d("响应数据:");
                        XLog.json(new String(resultBytes));
                        is.close();

                        // 回写操作,否则APP无法拿到数据进行展示
                        Object iss = Reflect.on("com.sankuai.meituan.retrofit2.InputStreamSubject", classLoader).create(new ByteArrayInputStream(resultBytes));
                        Reflect.on(ResponseBody).set("inputStreamSubject", iss);
                        Reflect.on(ResponseBody).call("close");
                    }
                }
            });
            XLog.d("monitor_okttp succeeded!");
        }
        catch(Throwable e)
        {
            XLog.d("monitor_okttp error!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public byte[] convertStreamToString(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (true) {
            int len = is.read(buffer);
            if (len == -1) {
                break;
            }
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }
}
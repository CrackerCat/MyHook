package org.xk.xpexample.backend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class OkHttpHook
{
    public void HookOkHttpMoudle(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> aClassRealInterceptorChain = XposedHelpers.findClass("okhttp3.internal.http.RealInterceptorChain", lpparam.classLoader);
        Class<?> okhttp3 = XposedHelpers.findClass("okhttp3.Request", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(aClassRealInterceptorChain, "proceed", okhttp3, new XC_MethodHook()
        {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                super.afterHookedMethod(param);

                //入参
                Object arg = param.args[0];
                StringBuilder netStr = new StringBuilder();
                netStr.append("\n");
                netStr.append(">>>> start net");
                netStr.append("\n");
                netStr.append("procee@@@@@@arg.getClass:").append(arg.getClass());
                netStr.append("\n");
                netStr.append("proceed@@arg:").append(arg);

                Class<?> aClassRequest = arg.getClass();
                //获取body
                Method requestBodyMethod = aClassRequest.getDeclaredMethod("body");
                Object invokeRequestBody = requestBodyMethod.invoke(arg);
                boolean hasRequestBody = invokeRequestBody != null;

                Method methodMethod = aClassRequest.getDeclaredMethod("method");
                Object invokeMethod = methodMethod.invoke(arg);

                if (hasRequestBody)
                {
                    Class<?> invokeBodyClass = invokeRequestBody.getClass();
                    // Request body headers are only present when installed as a network interceptor. Force
                    // them to be included (when available) so there values are known.
                    Method contentTypeMethod = invokeBodyClass.getDeclaredMethod("contentType");
                    Object invokeContentType = contentTypeMethod.invoke(invokeRequestBody);
                    if (invokeContentType != null)
                    {
                        netStr.append("\n");
                        netStr.append("Content-Type: ").append(invokeContentType);
                    }
                    Method contentLengthMethod = invokeBodyClass.getDeclaredMethod("contentLength");
                    Object invokeContentLength = contentLengthMethod.invoke(invokeRequestBody);
                    if (invokeContentLength != null)
                    {
                        netStr.append("\n");
                        netStr.append("Content-Length: ").append(invokeContentLength);
                    }
                    Method headersMethod = aClassRequest.getDeclaredMethod("headers");
                    Object invokeHeaders = headersMethod.invoke(arg);

                    if (invokeHeaders != null)
                    {
                        Class<?> aClassHeaders = invokeHeaders.getClass();
                        Method sizeMethod = aClassHeaders.getDeclaredMethod("size");
                        int invokeSize = (int) sizeMethod.invoke(invokeHeaders);
                        for (int i = 0; i < invokeSize; i++)
                        {
                            Method nameMethod = aClassHeaders.getDeclaredMethod("name", int.class);
                            String name = (String) nameMethod.invoke(invokeHeaders, i);
                            // Skip headers from the request body as they are explicitly logged above.
                            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name))
                            {
                                netStr.append("\n");
                                netStr.append(name).append(": ").append(aClassHeaders.getDeclaredMethod("value", int.class).invoke(invokeHeaders, i));
                            }
                        }
                        //处理结束地方
                        if (!hasRequestBody)
                        {
                            netStr.append("\n");
                            netStr.append("--> END ").append(invokeMethod);
                        }
                        else if (bodyEncoded(invokeHeaders))
                        {
                            netStr.append("\n");
                            netStr.append("--> END ").append(invokeMethod).append(" (encoded body omitted)");
                        }
                    }
                }

                //出参
                Object result = param.getResult();
                if (result != null)
                {
                    netStr.append("\n");
                    netStr.append("proceed@@result:").append(result);
                    Class<?> aClassResponse = result.getClass();
                    Method headersMethod = aClassResponse.getDeclaredMethod("headers");
                    Object invokeHeaders = headersMethod.invoke(result);
                    boolean isGzip = false;

                    if (invokeHeaders != null)
                    {
                        Class<?> aClassHeaders = invokeHeaders.getClass();
                        Method sizeMethod = aClassHeaders.getDeclaredMethod("size");
                        int invokeSize = (int) sizeMethod.invoke(invokeHeaders);
                        for (int i = 0; i < invokeSize; i++)
                        {
                            Method nameMethod = aClassHeaders.getDeclaredMethod("name", int.class);
                            String name = (String) nameMethod.invoke(invokeHeaders, i);
                            String value = (String) aClassHeaders.getDeclaredMethod("value", int.class).invoke(invokeHeaders, i);
                            if ("Content-Encoding".equalsIgnoreCase(name) && "gzip".equals(value))
                            {
                                //标记下数据是否压缩
                                isGzip = true;
                            }
                            netStr.append("\n");
                            netStr.append(name).append(": ").append(value);
                        }
                    }
                    Method bodyResponseMethod = aClassResponse.getDeclaredMethod("body");
                    Object invokeResponseBody = bodyResponseMethod.invoke(result);
                    Class<?> aClassResponseBody = invokeResponseBody.getClass();
                    Method sourceMethod = aClassResponseBody.getDeclaredMethod("source");
                    Object invokeBufferedSource = sourceMethod.invoke(invokeResponseBody);
                    //okio.e  BufferedSource
                    Class<?> aClassBufferedSource = invokeBufferedSource.getClass();
                    //b request
                    Method requestMethod;//request
                    try {
                        requestMethod = aClassBufferedSource.getDeclaredMethod("request", long.class);//request
                    } catch (Exception e) {
                        requestMethod = aClassBufferedSource.getDeclaredMethod("b", long.class);//request
                    }
                    requestMethod.invoke(invokeBufferedSource, Long.MAX_VALUE);
                    // c b() buffer
                    Method bufferMethod;//buffer
                    try {
                        bufferMethod = aClassBufferedSource.getDeclaredMethod("buffer");//buffer
                    } catch (Exception e) {
                        bufferMethod = aClassBufferedSource.getDeclaredMethod("b");//buffer
                    }
                    Object invokeBuffer = bufferMethod.invoke(invokeBufferedSource);
                    Class<?> aClassBuffer = invokeBuffer.getClass();
                    Method cloneMethod = aClassBuffer.getDeclaredMethod("clone");
                    Object invoke = cloneMethod.invoke(invokeBuffer);
                    Class<?> aClass = invoke.getClass();
                    Method readString = null;//readString
                    try {
                        readString = aClass.getDeclaredMethod("readString", Charset.class);//readString
                    } catch (Exception e) {
                        readString = aClass.getDeclaredMethod("a", Charset.class);//readString
                    }
                    String repResult = (String) readString.invoke(invoke, Charset.forName("UTF-8"));

                    netStr.append("\n");
                    netStr.append("rep result:").append(isGzip ? new String(uncompress(repResult.getBytes())) : repResult);
                }

                netStr.append("\n");
                netStr.append(">>>> end net");
                XposedBridge.log("okhttp:" + netStr.toString());
            }
        });
    }

    private boolean bodyEncoded(Object invokeHeaders) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        String contentEncoding = (String) invokeHeaders.getClass().getDeclaredMethod("get", String.class).invoke(invokeHeaders, "Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public static byte[] uncompress(byte[] bytes)
    {
        if (bytes == null || bytes.length == 0)
            return null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        try
        {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0)
                out.write(buffer, 0, n);
            return out.toByteArray();
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            return bytes;
        }
    }
}

# Frida简介
	官网：https://frida.re
	源码：https://github.com/frida/frida

## 环境搭建
	// 安装
	$ push FridaSrv /data/local/tmp/
	$ chmod 7755 /data/local/tmp/FridaSrv
	$ ./FridaSrv
	// 运行
	$ frida-ps -U
	$ frida-trace -U -i open com.android.chrome
      Attaching...
	  Resolving functions...
	  Instrumenting functions...
	  open: Auto-generated handler at "D:\Python\2.7.8\Scripts\__handlers__\libc.so\open.js"
	  Started tracing 1 function. Press Ctrl+C to stop.

## Frida Hook Java
	打印参数
	修改返回
	主动调用
	  静态函数
	  非静态函数
	修改成员变量
	Hook内部类
	Hook动态加载Dex
	--------------
	Frida spawn 启动App
	调用系统库函数
	Hook 构造函数
	Frida动态加载Dex
	RegisterClass
	打印调用栈
	
## Frida Hook Native
	Hook Java层JNI函数
	Hook so 导出函数 获取基地址
	枚举模块的符号
	Hook libart的一些函数
	打印Native调用栈
	Hook android_dlopen_ext
	Frida 读写文件
	Frida 调用C函数
	
## Frida 辅助分析OLLVM字符串加密
	.init_arry 里的字符串加密函数
	完善hook libart
	Frida inline hook
	
## Frida 辅助分析OLLVM控制流平坦化
	控制流程平坦化简介
	Frida和IDA分析控制流平坦化
	MD5特征&Base64特征
	
## Frida 辅助分析OLLVM指令替换
	OLLVM指令替换简介
	Frida和IDA分析指令替换
	
## Frida 辅助分析OLLVM虚假控制流
	OLLVM虚假控制流简介
	Frida和IDA分析虚假控制流

## Frida + IDA Trace 分析算法
	IDA Trace
	从 Trace 日志中分析算法
	根据 Trace 日志还原算法
	Frida CModule 测试算法正确性
	
## Frida Stalker Trace 算法
	Frida Stalker 使用
	Frida Stalker Trace
	

	

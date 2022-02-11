# Frida

## 使用
	$ push FridaSrv /data/local/tmp/
	$ chmod 7755 /data/local/tmp/FridaSrv
	$ ./FridaSrv
	$ frida-ps -U
	$ frida-trace -U -i open com.android.chrome
      Attaching...
	  Resolving functions...
	  Instrumenting functions...
	  open: Auto-generated handler at "D:\Python\2.7.8\Scripts\__handlers__\libc.so\open.js"
	  Started tracing 1 function. Press Ctrl+C to stop.

## 
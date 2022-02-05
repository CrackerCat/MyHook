# 说明
	strace是linux下调试利器,类似jnitrace可以打印系统调用的参数返回值。一般用来追踪文件操作。

# 使用方法
	adb shell setenforce 0
	
	// 附加到正在运行的进程
	strace -f -p PID -o strace_log.txt

	-e 
	    trace=file           跟踪和文件访问相关的调用(参数中有文件名)
    	trace=process        和进程管理相关的调用，比如fork/exec/exit_group
	    trace=network        和网络通信相关的调用，比如socket/sendto/connect
	    trace=signal         信号发送和处理相关，比如kill/sigaction
	    trace=desc           和文件描述符相关，比如write/read/select/epoll等
	    trace=ipc            进程见通讯相关，比如shmget等

# 参数
	-tt 在每行输出的前面，显示毫秒级别的时间
	-T 显示每次系统调用所花费的时间
	-v 对于某些相关调用，把完整的环境变量，文件stat结构等打出来。
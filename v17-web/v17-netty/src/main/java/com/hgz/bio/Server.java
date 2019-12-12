package com.hgz.bio;
import java.net.Socket;
import	java.util.concurrent.LinkedBlockingQueue;
import	java.util.concurrent.ThreadPoolExecutor;
import java.io.IOException;
import	java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) throws IOException {
        //服务端开启端口监听客户端连接
        ServerSocket serverSocket = new ServerSocket(8888);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,4,1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable> (10));
        //接受客户端的连接请求
        while (true){
            Socket socket = serverSocket.accept();
        //3.创建处理任务，交给线程池执行
            pool.execute(new HandleTask(socket));

        }
    }
}

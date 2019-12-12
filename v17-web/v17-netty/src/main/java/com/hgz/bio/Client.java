package com.hgz.bio;
import	java.io.OutputStream;
import java.io.IOException;
import	java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //创建socket连接服务端
        Socket socket = new Socket("localhost",8888);
        //获取输出流
        OutputStream outputStream = socket.getOutputStream();
        //给服务端发送数据
        outputStream.write("hello,BIO server".getBytes());
        outputStream.close();
        socket.close();
    }
}

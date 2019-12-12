package com.hgz.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HandleTask implements Runnable {

    private Socket socket;

    public HandleTask(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            //read方法是阻塞式的，客户端没有发送数据过来前，会一直在此等待
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println("客户端收到的消息"+new String(bytes, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

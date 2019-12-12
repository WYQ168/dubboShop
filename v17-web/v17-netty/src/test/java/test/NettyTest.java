package test;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NettyTest {
    @Test
    public void writeTest() throws Exception {
        //1.创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("nio.txt");
        //2.通过输出流创建一个通道
        FileChannel channel = fileOutputStream.getChannel();
        //3.创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4.将字节数据写入到缓冲区中
        buffer.put("hello,nio".getBytes());
        //5.注意，此时缓冲区的指针已经随着放入的数据而发生偏移
        //所以，需要重置
        buffer.flip();
        //6.将缓冲区的数据写入通道，通道负责将数据写入到文件中
        channel.write(buffer);
        //7.关闭资源
        fileOutputStream.close();
    }

    @Test
    public void readTest() throws IOException {
        //1.创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("nio.txt");
        //2.通过输入流创建一个通道
        FileChannel channel = fileInputStream.getChannel();
        //3.创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4.从通道中读取数据，并写入到缓冲区中
        channel.read(buffer);
        //5.输出读取到的数据
        System.out.println(new String(buffer.array()));
        //6.关闭资源
        fileInputStream.close();
    }

    @Test
    public void copyTest() throws Exception{
        //1.创建输入流和输出流
        FileInputStream fileInputStream =  new FileInputStream("nio.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("nio_copy.txt");
        //2.通过流创建两个通道
        FileChannel source = fileInputStream.getChannel();
        FileChannel target = fileOutputStream.getChannel();
        //3.通过通道实现快速拷贝
        //target.transferFrom(source,0,source.size());
        source.transferTo(0,source.size(),target);
        //4.关闭资源
        fileOutputStream.close();
        fileInputStream.close();
    }
}

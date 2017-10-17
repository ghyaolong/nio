package com.yao.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestBlockingNIO {


    @Test
    public void client() throws IOException {

        //获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));

        FileChannel fileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

        //分配制定大小缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //读取本地文件，并发送到服务器
        while (fileChannel.read(byteBuffer)!=-1){
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        fileChannel.close();
        socketChannel.close();

    }

    //服务端
    @Test
    public void server() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel fileChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //绑定链接
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //获取客户端链接的通道
        SocketChannel accept = serverSocketChannel.accept();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (accept.read(buffer)!=-1){
            buffer.flip();
            accept.write(buffer);
            buffer.clear();
        }

        accept.close();
        fileChannel.close();
        serverSocketChannel.close();
    }


    @Test
    public void test(){
        char c =3;
        byte b =-128;
        byte b1 =127;

        String aa="你";
        char[] chars = aa.toCharArray();
       /* for (int i = 0; i < chars.length; i++) {
            System.out.println(chars[i]);
        }*/
        char df=65535;
        byte[] bytes = aa.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(bytes[i]);
        }

    }
}

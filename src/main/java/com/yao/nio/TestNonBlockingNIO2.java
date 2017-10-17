package com.yao.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

public class TestNonBlockingNIO2 {

    @Test
    public void send() throws IOException {
        DatagramChannel dgChannel = DatagramChannel.open();
        dgChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()){
            String str = scan.next();
            buffer.put((LocalDateTime.now().toString()+" "+str).getBytes());
            buffer.flip();
            dgChannel.send(buffer,new InetSocketAddress("127.0.0.1",9999));
            buffer.clear();
        }
        dgChannel.close();
    }

    @Test
    public void receive() throws IOException {
        DatagramChannel dgChannel = DatagramChannel.open();
        dgChannel.configureBlocking(false);
        dgChannel.bind(new InetSocketAddress(9999));

        Selector selector = Selector.open();
        dgChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select()>0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey sk = it.next();
                if (sk.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    dgChannel.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(),0,buf.limit()));
                    buf.clear();
                }
                it.remove();
            }
        }
    }
}

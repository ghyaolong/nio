package com.yao.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestChannel {


    //编码集
    @Test
    public void test5() throws CharacterCodingException {
        Charset cs = Charset.forName("GBK");
        CharsetEncoder charsetEncoder = cs.newEncoder();
        CharsetDecoder charsetDecoder = cs.newDecoder();

        CharBuffer cb = CharBuffer.allocate(1024);
        cb.put("你好天天向上！");
        cb.flip();
        System.out.println(cb.toString().getBytes().length);

        //编码
        ByteBuffer encode = charsetEncoder.encode(cb);

        for (int i = 0; i < 14; i++) {
            System.out.println(encode.get());

        }

        encode.flip();
        //解码
        CharBuffer decode = charsetDecoder.decode(encode);
        System.out.println(decode.toString());
    }

    //分散  聚集
    @Test
    public void test4() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");

        FileChannel raf1Channel = raf1.getChannel();

        //分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        ByteBuffer[] bufs = {buf1, buf2};

        //分散读取
        raf1Channel.read(bufs);

        for (ByteBuffer byteBuffer : bufs) {
            byteBuffer.flip();
        }

        System.out.println(new String(bufs[0].array(),0,bufs[0].limit()));

        System.out.println("------------------------------");
        System.out.println(new String(bufs[1].array(),0,bufs[1].limit()));

    }

    //通道之间的数据传输（直接缓冲区）
    @Test
    public void test3() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inChannel.close();
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //使用直接缓冲区完成文件的复制(内存映射文件)
    @Test
    public void test2() {
        try {
            FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

            //内存映射文件
            MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());


            //直接对缓冲区进行数据的操作
            byte[] dst = new byte[inMappedBuf.limit()];
            inMappedBuf.get(dst);
            outMappedBuf.put(dst);

            inChannel.close();
            outChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //利用通道完成文件的复制
    @Test
    public void test1() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fisChannel = null;
        FileChannel fosChannel = null;
        try {
            fis = new FileInputStream("1.jpg");
            fos = new FileOutputStream("2.jpg");

            //File file = new File("1.jpg");

            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();
           // System.out.println("size:"+fisChannel.size());

            ByteBuffer buf = ByteBuffer.allocate(1024);

            while (fisChannel.read(buf) != -1) {
                buf.flip();
                fosChannel.write(buf);
                buf.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fisChannel != null) {
                    fisChannel.close();
                }
                if (fosChannel != null) {
                    fosChannel.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}

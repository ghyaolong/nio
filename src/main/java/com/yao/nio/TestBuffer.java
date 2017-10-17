package com.yao.nio;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Scanner;

public class TestBuffer {

    @Test
    public void test2(){
        String str = "abcde";
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(str.getBytes());
        buf.flip();
        byte[] dst = new byte[buf.limit()];
        buf.get(dst,0,2);
        System.out.println(new String(dst,0,2));

        buf.mark();
        buf.get(dst,2,2);
        System.out.println(new String(dst,0,2));

        buf.reset();
        System.out.println(buf.position());
    }
    @Test
    public void test1(){
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.clear();
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String str = sc.next();
            System.out.println(str);
        }
    }
}

package com.study;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufferDemo {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);
        System.out.println("原始ByteBuf为==================>" + buf.toString());
        System.out.println("1.ByteBuf中的内容==============>" + Arrays.toString(buf.array()) + "\n");

        // 写入一段内容
        byte[] bytes = {1, 2, 3, 4, 5};
        buf.writeBytes(bytes);
        System.out.println("写入的bytes为==================>" + Arrays.toString(bytes));
        System.out.println("写入内容后的ByteBuf为===========>" + buf.toString());
        System.out.println("2.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 读取内容
        byte b1 = buf.readByte();
        byte b2 = buf.readByte();
        System.out.println("读取的bytes为==================>" + Arrays.toString(new byte[] {b1, b2}));
        System.out.println("读取内容后的ByteBuf为===========>" + buf.toString());
        System.out.println("3.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 丢弃内容
        buf.discardReadBytes();
        System.out.println("丢弃内容后的ByteBuf为===========>" + buf.toString());
        System.out.println("4.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 清空读取指针
        buf.clear();
        System.out.println("清空指针后的ByteBuf为===========>" + buf.toString());
        System.out.println("5.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 再写入一段内容
        byte[] bytes2 = {1, 2, 3};
        buf.writeBytes(bytes2);
        System.out.println("写入的bytes为==================>" + Arrays.toString(bytes));
        System.out.println("写入内容后的ByteBuf为===========>" + buf.toString());
        System.out.println("6.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 将buffer清零
        buf.setZero(0, buf.capacity());
        System.out.println("清零内容后的ByteBuf为===========>" + buf.toString());
        System.out.println("7.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 再写入一段内容
        byte[] bytes3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        buf.writeBytes(bytes3);
        System.out.println("写入的bytes为==================>" + Arrays.toString(bytes));
        System.out.println("写入内容后的ByteBuf为===========>" + buf.toString());
        System.out.println("8.ByteBuf中的内容===============>" + Arrays.toString(buf.array()) + "\n");

        // 再写入一段内容,超过4M
        byte[] bytes4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int i = 0; i < 65*1024*4; i++) {
            buf.writeBytes(bytes4);
        }
        System.out.println("写入内容后的ByteBuf为===========>" + buf.toString());
    }
}
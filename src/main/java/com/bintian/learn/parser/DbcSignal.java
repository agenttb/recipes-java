package com.bintian.learn.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 1. 信号定义
public class DbcSignal {
    String name;
    int startBit;
    int length;
    String byteOrder; // Intel (1) / Motorola (0)
    boolean isSigned; // - Signed, + Unsigned
    double factor;
    double offset;
    double min;
    double max;
    String unit;
    List<String> receivers = new ArrayList<>();
    Map<Integer, String> valueTable = new HashMap<>();// 值描述 (例如 0="Off", 1="On")

    @Override
    public String toString() {
        return String.format("    Signal: %s [Start:%d Len:%d Factor:%.2f Offset:%.2f Unit:'%s']",
                name, startBit, length, factor, offset, unit);
    }


    // 2. 消息定义
    public static class DbcMessage {
        long id;
        String name;
        int size;
        String transmitter;
        List<DbcSignal> signals = new ArrayList<>();

        public DbcMessage(long id, String name, int size, String transmitter) {
            this.id = id;
            this.name = name;
            this.size = size;
            this.transmitter = transmitter;
        }

        @Override
        public String toString() {
            return String.format("Message ID: 0x%X Name: %s (Size: %d) Node: %s", id, name, size, transmitter);
        }
    }
}
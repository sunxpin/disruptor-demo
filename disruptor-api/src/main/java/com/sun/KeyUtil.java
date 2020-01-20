package com.sun;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.stream.IntStream;

public class KeyUtil {

    public static String generatorUUID() {
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        return timeBasedGenerator.generate().toString();
    }

    public static void main(String[] args) {
        System.err.println(KeyUtil.generatorUUID());
        System.err.println(KeyUtil.generatorUUID());
        IntStream.range(0, 20).forEach(i -> System.out.println(KeyUtil.generatorUUID()));
    }
}

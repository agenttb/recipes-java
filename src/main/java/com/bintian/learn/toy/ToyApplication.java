package com.bintian.learn.toy;

public class ToyApplication {
    public static void main(String[] args) {
        System.out.println(Math.abs(Integer.MIN_VALUE));
        System.out.println(Math.abs(Integer.MIN_VALUE + 1));

        String str = "1.2.3.4.5.6";
        String s = str.replaceAll("\\.", "");
        System.out.println("Replaced :" + s);
    }
}

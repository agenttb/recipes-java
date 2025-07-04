package com.bintian.learn.algorithm.solution.bitoperation;

public class Solution {

    public int hammingWeight(int n) {
        int cnt = 0;
        for (int i = 0; i < 32; i++) {
            if ((n >>> i & 1) == 1) {
                cnt++;
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(1));
    }
}

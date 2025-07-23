package com.bintian.learn.algorithm.solution.string;

import java.util.*;

public class Solution {
    public String reverseVowels(String s) {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('e');
        set.add('i');
        set.add('o');
        set.add('u');
        set.add('A');
        set.add('E');
        set.add('I');
        set.add('O');
        set.add('U');
        char[] array = s.toCharArray();
        int left = 0;
        int right = array.length-1;
        while (left < right) {
            while (!set.contains(array[left])) {
                left++;
            }
            while (!set.contains(array[right])) {
                right--;
            }
            if (left >= right) {
                break;
            }
            char temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
        return new String(array);
    }

    public String findLongestWords(String s, List<String> dictionary) {
        dictionary.sort((a, b) -> {
            if (a.length() != b.length()) {
                return a.length() - b.length();
            } else {
                return a.compareTo(b);
            }
        });
        for (String word : dictionary) {
            if (isSubsequence(word, s)) {
                return word;
            }
        }
        return "";
    }

    private boolean isSubsequence(String word, String s) {
        int i = 0;
        int j = 0;
        while (i < word.length() && j < s.length()) {
            if (word.charAt(i) == s.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == word.length();
    }

    public static String biggerIsGreater(String w) {
        // 将字符串转换为字符数组，方便操作
        char[] chars = w.toCharArray();
        int n = chars.length;

        // 步骤 1: 从右向左找到枢轴点 i，即第一个 chars[i] < chars[i+1] 的位置
        int i = n - 2;
        while (i >= 0 && chars[i] >= chars[i + 1]) {
            i--;
        }

        // 步骤 2: 如果没有找到枢轴点，说明整个字符串是降序的，已经是最大排列
        if (i < 0) {
            return "no answer";
        }

        // 步骤 3: 从右向左找到第一个大于枢轴点的字符 chars[j]
        int j = n - 1;
        while (j >= 0 && chars[j] <= chars[i]) {
            j--;
        }

        // 步骤 4: 交换枢轴点和找到的字符
        swap(chars, i, j);

        // 步骤 5: 反转枢轴点之后的所有字符，使其变为升序
        reverse(chars, i + 1, n - 1);

        // 将字符数组转换回字符串并返回
        return new String(chars);
    }

    /**
     * 辅助函数：交换字符数组中的两个元素
     */
    private static void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    /**
     * 辅助函数：反转字符数组的指定部分
     */
    private static void reverse(char[] chars, int start, int end) {
        while (start < end) {
            swap(chars, start, end);
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        List<String> list = new ArrayList<>();
        list.add("ab");
//        list.add("bb");
//        list.add("hefg");
//        list.add("dhck");
//        list.add("dkhc");
//        list.add("abdc");
        for (String str : list) {
            System.out.println(str);
            var res = solution.biggerIsGreater(str);
            System.out.println(res);
        }
    }
}

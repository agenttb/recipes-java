package com.bintian.learn.algorithm.solution.string;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static void main(String[] args) {
        Solution solution = new Solution();
        String leetcode = solution.reverseVowels("leetcode");
        System.out.println(leetcode);

        String a = "330";
        String b = "303";
        System.out.println(a.compareTo(b));
    }
}

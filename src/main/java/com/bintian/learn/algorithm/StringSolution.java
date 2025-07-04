package com.bintian.learn.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StringSolution {
    public String validIPAddress(String queryIP) {
        if (queryIP == null) {
            return "Neither";
        }
        String[] ipv4s = queryIP.split("\\.");
        String[] ipv6s = queryIP.split(":");
        if (ipv4s.length != 4 && ipv6s.length != 8) {
            return "Neither";
        }
        if (ipv4s.length == 4) {
            return validIPV4(ipv4s);
        } else {
            return validateIPv6(queryIP);
        }
    }

    private String validIPV4(String[] ips) {
        for (String x : ips) {
            if (x.length() > 3 || x.isEmpty()) {
                return "Neither";
            }
            if (x.charAt(0) == '0' && x.length() != 1 ) {
                return "Neither";
            }
            for (char c: x.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return "Neither";
                }
            }
            if (Integer.parseInt(x) > 255) {
                return "Neither";
            }
        }
        return "IPv4";
    }
    public String validateIPv6(String IP) {
        String[] nums = IP.split(":", -1);
        String hexdigits = "0123456789abcdefABCDEF";
        for (String x : nums) {
            if (x.isEmpty() || x.length() > 4) {
                return "Neither";
            }
            for (char c : x.toCharArray()) {
                if (hexdigits.indexOf(c) == -1) {
                    return "Neither";
                }
            }
        }
        return "IPv6";
    }

    public static void main(String[] args) {
        int[] nums1 = {2,2,4,4};
        int[] nums2 = {2, 2, 2, 4, 4};
        StringSolution solution = new StringSolution();
        var res = solution.findMedianSortedArrays(nums1, nums2);
        System.out.println(res);
    }


    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;

        int left = 0;
        int right = m;

        // median1：nums1 中左半部分的最大值
        // median2：nums2 中左半部分的最大值
        int median1 = 0;
        int median2 = 0;

        while (left <= right) {
            // 分割线在 nums1 中的位置为 i，分割线左边有 i 个元素
            int i = (left + right) / 2;
            // 分割线在 nums2 中的位置为 j，左边有 j = (m + n + 1)/2 - i 个元素
            int j = (m + n + 1) / 2 - i;

            // 边界情况处理
            int maxLeft1 = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            int minRight1 = (i == m) ? Integer.MAX_VALUE : nums1[i];

            int maxLeft2 = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            int minRight2 = (j == n) ? Integer.MAX_VALUE : nums2[j];

            // 判断是否找到了正确划分
            if (maxLeft1 <= minRight2) {
                // 继续向右搜索
                median1 = Math.max(maxLeft1, maxLeft2);
                median2 = Math.min(minRight1, minRight2);
                left = i + 1;
            } else {
                // 向左搜索
                right = i - 1;
            }
            System.out.printf(" i:%d, j:%dm, maxLeft1:%d, minRight1:%d, maxLeft2:%d, minRight2:%d",
                    i,j, maxLeft1, minRight1, maxLeft2, minRight2);
            System.out.println();
        }

        // 如果总长度是奇数，中位数就是左半部分最大值
        // 如果是偶数，则是左右两部分最大值和最小值的平均值
        return (m + n) % 2 == 0 ? (median1 + median2) / 2.0 : median1;



    }

    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int distance = s.charAt(0) - t.charAt(0);
        int i = 1;
        for (; i < s.length(); i++) {
            if ( (s.charAt(i) - distance) != t.charAt(i)) {
                break;
            }
        }
        if ((i + 1) < s.length()) {
            return false;
        }
        return true;
    }

    public int countTarget(int[] scores, int target) {
        if (scores == null || scores.length == 0) return 0;

        int left = findFirst(scores, target);
        if (left == -1) return 0; // 没有找到 target
        int right = findLast(scores, target);

        return right - left + 1;
    }
    private int findLast(int[] scores, int target) {
        int start = 0;
        int end = scores.length - 1;

        while (start <= end) {
            int mid = start + (end - start)/2 ;
            if (scores[mid] <= target) {
                start = mid + 1;
            } else  {
                end = mid - 1;
            }
        }
        return end;
    }

    private int findFirst(int[] scores, int target) {
        int start = 0;
        int end = scores.length - 1;

        while (start <= end) {
            int mid = start + (end - start)/2 ;
            if (scores[mid] < target) {
                start = mid + 1;
            } else if (target >= scores[mid]) {
                end = mid - 1;
            }
        }
        return (start < scores.length && scores[start] == target) ? start : -1;
    }
}

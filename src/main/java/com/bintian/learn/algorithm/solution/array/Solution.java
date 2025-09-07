package com.bintian.learn.algorithm.solution.array;

import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        var res  = solution.checkInclusion("ab", "eidbaooo");
        System.out.println(res);

    }

    public int[] twoSum(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            int t = binarySearch(numbers, i + 1, target - numbers[i]);
            System.out.println("binarySearch : ---> " + t);
            if (t != -1) {
                return new int[]{i + 1, t + 1};
            }
        }
        return new int[2];

    }

    private int binarySearch(int[] numbers, int start, int target) {
        int left = start;
        int right = numbers.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (numbers[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        if (left < numbers.length && numbers[left] == target) {
            return left;
        } else {
            return -1;
        }
    }

    public int[][] fileCombination(int target) {
        int l = 1;
        int r = 1;
        int sum = 0;
        List<int[]> res = new ArrayList<>();
        while (l <= target / 2) {
            if (sum < target) {
                sum += r;
                r++;
            } else if (sum > target) {
                sum -= l;
                l++;
            } else {
                int[] seq = new int[r - l];
                for (int i = l; i < r; i++) {
                    seq[i - l] = i;
                }
                res.add(seq);
                sum -= l;
                l++;
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    public int trappingRainWater(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int n = height.length;
        int[] maxLeft = new int[n];
        int[] maxRight = new int[n];
        maxLeft[0] = height[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i-1], height[i]);
        }
        maxRight[n-1] = height[n-1];
        for (int i = n-2; i > -1; i--) {
            maxRight[i] = Math.max(maxRight[i+1], height[i]);
        }
        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            int waterLevel = Math.min(maxLeft[i], maxRight[i]);
            totalWater += waterLevel - height[i];
        }
        return totalWater;
    }

    public int trapForDoublePointer(int[] waters) {
        int totalWater = 0;
        int maxLeft = 0;
        int maxRight = 0;
        int left = 0;
        int right = waters.length-1;
        while (left < right) {
            if (waters[left] < waters[right]) {
                maxLeft = Math.max(maxLeft, waters[left]);
                totalWater += maxLeft - waters[left];
                left++;
            } else {
                maxRight = Math.max(maxRight, waters[right]);
                totalWater += maxRight - waters[right];
                right--;
            }
        }
        return totalWater;
    }

    public int singleNonDuplicate(int[] nums) {
        int left = 1;
        int right = nums.length - 2;
        while ((left-1) < (right+1)) {
            int target1 = nums[left] + nums[right];
            int target2 = nums[left-1] + nums[right+1];
            if (target1 != target2) {
                return nums[left] == nums[left -1] ? nums[right+1] : nums[left -1] ;
            } else {
                left += 2;
                right -= 2;
            }
        }
        return -1;
    }

    public boolean checkInclusion(String s1, String s2) {
        int n1 = s1.length();
        int n2 = s2.length();
        if (n1 > n2) {
            return false;
        }
        int[] s1Freq = new int[26];
        int[] windowsFreq = new int[26];
        for (int i = 0; i < n1; i++) {
            s1Freq[s1.charAt(i) - 'a']++;
            windowsFreq[s2.charAt(i) - 'a']++;
        }

        if (Arrays.equals(s1Freq, windowsFreq)) {
            return true;
        }

        for (int right = n1; right < n2; right++) {
            windowsFreq[s2.charAt(right) - 'a']++;
            int leftIndex = right - n1;
            windowsFreq[s2.charAt(leftIndex) - 'a']--;
            if (Arrays.equals(s1Freq, windowsFreq)) {
                return true;
            }

        }
        return false;
    }
}

package com.bintian.learn.algorithm.solution.sort;

import java.util.*;

public class SortSolution {
    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        int left = 0;
        int right = n-1;
        int target = n - k;
        while (left <= right) {
            int pivotIndex = partition(nums, left, right);
            if (target == pivotIndex) {
                return nums[pivotIndex];
            } else if (pivotIndex < target) {
                left = pivotIndex + 1;
            } else {
                right = pivotIndex - 1;
            }
        }
        return -1;
    }

    private int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (nums[j] < pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, right);
        return i;
    }

    private void swap(int nums[], int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        SortSolution solution = new SortSolution();
        var re = solution.frequencySort("tree");
        System.out.println(re);
    }

    public String frequencySort(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            map.put(s.charAt(i), map.getOrDefault(c, 0) + 1);
        }
        Queue<int[]> queue =  new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (var entry : map.entrySet()) {
            queue.offer(new int[]{entry.getKey() - 'a', entry.getValue()});
        }
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            var a = queue.poll();
            int size = a[1];
            char c = (char) ('a' + a[0]);
            for (int i = 0; i < size; i++) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

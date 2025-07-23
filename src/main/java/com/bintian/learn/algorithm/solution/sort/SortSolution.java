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

    public int[] countServersSlidingWindows(int n, int[][] logs, int x, int[] queries) {
        Arrays.sort(logs, Comparator.comparingInt(a -> a[1]));
        int[][] sortedQueries = new int[queries.length][2];
        for (int i = 0; i < queries.length; i++) {
            sortedQueries[i][0] = queries[i];
            sortedQueries[i][1] = i;
        }
        Arrays.sort(sortedQueries, Comparator.comparingInt(a -> a[0]));
        int[] result = new int[queries.length];
        Map<Integer, Integer> serverCounts = new HashMap<>();

        int left = 0;
        int right = 0;

        for (int[] query : sortedQueries) {
            int endTime = query[0];
            int originalIndex = query[1];
            int startTime = endTime - x;

            while (right < logs.length && logs[right][1] <= endTime) {
                int serverId = logs[right][1];
                serverCounts.put(serverId, serverCounts.getOrDefault(serverId, 0) + 1);
                right++;
            }

            while (left < right && logs[left][1] < startTime) {
                int serverId = logs[left][1];
                serverCounts.put(serverId, serverCounts.get(serverId)-1);
                if (serverCounts.get(serverId) == 0) {
                    serverCounts.remove(serverId);
                }
                left++;
            }
            int activeServers = serverCounts.size();
            result[originalIndex] = n -activeServers;
        }
        return result;
    }

    public int[] countServers(int n, int[][] logs, int x, int[] queries) {
        Arrays.sort(logs, (a, b) -> a[1] - b[1]);
        int[] result = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int start = queries[i] - x;
            int end = queries[i];
            int endIndex = Arrays.binarySearch(logs, new int[]{end+1, end+1}, Comparator.comparingInt(a -> a[1]));

            int  startIndex = Arrays.binarySearch(logs, new int[]{start, start}, Comparator.comparingInt(a -> a[1]));
            if (startIndex < 0) {
                startIndex = -startIndex - 1;
            }
            if (endIndex < 0) {
                endIndex = -endIndex - 1;
            }
            Set<Integer> serverCnt = new HashSet<>();
            for (int j = startIndex; j <= endIndex && j < logs.length; j++) {
                if (logs[j][1] >= start && logs[j][1] <= end) {
                    serverCnt.add(logs[j][0]);
                }
            }
            result[i] = n - serverCnt.size();
        }
        return result;
    }

    public int binarySearchLeftmost(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            int mid = left + (right - left)/2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public int binarySearchRightmost(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            int mid = left + (right - left)/2;
            if (nums[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return right - 1;
    }

    public static void main(String[] args) {
        SortSolution solution = new SortSolution();
//        int[] ints = solution.countServers(3, new int[][]{{2, 4}, {2, 1}, {1, 2}, {3, 1}}, 2, new int[]{3, 4});
//        System.out.println(Arrays.toString(ints));

        int[] p = new int[] {1,2,3,4,4,4,4,4,6,6,7,8,9};
//        int index = Arrays.binarySearch(p, 4);
//        System.out.println(index);

        int index1 = solution.binarySearchLeftmost(p, 5);
        System.out.println(index1);

        int index2 = solution.binarySearchRightmost(p, 5);
        System.out.println(index2);

    }

}

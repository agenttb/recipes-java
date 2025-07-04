package com.bintian.learn.algorithm;

import java.util.*;

public class ArraySolution {
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> matrix[a[0]][a[1]]));
        for (int i = 0; i < n; i++) {
            minHeap.offer(new int[] {i, 0});
        }
        for (int j = 0; j < k -1; j++) {
            int[] pos = minHeap.poll();
            int row = pos[0];
            int col = pos[1] + 1;
            if (col < n) {
                minHeap.offer(new int[] {row, col});
            }
        }
        int[] top = minHeap.peek();
        return matrix[top[0]][top[1]];
    }

    public int kthSmallestBS(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int low = matrix[0][0];
        int high = matrix[n-1][n-1];
        while (low <= high) {
            int mid = low + (high - low)/2;
            int count = 0;
            for (int i = 0; i < m ; i++) {
                for (int j = 0; j < n && matrix[i][j] <= mid; j++) {
                    count++;
                }
            }
            if (count < k) {
                low  = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    public int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        Arrays.sort(intervals, (a, b) -> a[0] = b[0]);
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            if (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }
            minHeap.offer(end);
        }
        return minHeap.size();
    }

    public static void main(String[] args) {
        ArraySolution solution = new ArraySolution();
        int[][] matrix = {
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}};
//        var res = solution.kthSmallest(matrix, 5);
//        var res1 = solution.kthSmallestBS(matrix, 5);
//        System.out.println(res);
//        System.out.println(res1);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                list.add(matrix[i][j]);
            }
        }
        Map<Integer, Integer> map = new HashMap<>();

        int[] array = map.values().stream().sorted().mapToInt(Integer::intValue).toArray();
        Collections.sort(list);
        System.out.println(list);
    }
}

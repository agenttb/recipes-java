package com.bintian.learn.algorithm.solution.sort;

import org.springframework.data.util.Pair;

import java.util.*;

public class MedianFinder {
    private final PriorityQueue<Integer> maxHeap;
    private final PriorityQueue<Integer> minHeap;
    private int size;
    public MedianFinder() {
        this.maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        this.minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.add(num);
        } else {
            minHeap.add(num);
        }
        if (maxHeap.size() > minHeap.size() +1) {
            maxHeap.add(minHeap.peek());
        }
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.add(minHeap.poll());
        }
        size++;
    }

    public double findMedian() {
        if (size == 0) {
            return 0.0;
        }
        if ((size & 1) == 1) {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            return maxHeap.peek();
        }
    }

    public static void main(String[] args) {
        MedianFinder finder = new MedianFinder();
//        int[] ints = finder.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
//        System.out.println(Arrays.toString(ints));

        int[] ints2 = finder.topKFrequent(new int[] {5,3,1,1,1,3,73,1}, 2);
        System.out.println(Arrays.toString(ints2));
    }

    public int[] topKFrequent(int[] nums, int k) {
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            int freq = map.getOrDefault(num, 0) +1;
            map.put(num, freq);

        }
        for (var entry : map.entrySet()) {
            minHeap.offer(new int[] {entry.getValue(), entry.getKey()});
//            if (minHeap.size() > k) {
//            }
            //minHeap.poll();
        }
        int[] res = new int[k];
        int index = k -1 ;
        while (!minHeap.isEmpty()) {
            res[index--] = minHeap.poll()[1];
        }
        return res;
    }

    public int[] topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int[] res = new int[k];
        Queue<Pair<Integer, Integer>> maxHeap = new PriorityQueue<Pair<Integer, Integer>>((Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) -> {
            if (p1.getSecond() < p2.getSecond()) {
                return 1;
            } else if (p1.getSecond().equals(p2.getSecond())){
                return 0;
            } else {
                return -1;
            }
        });
        for (Integer value : map.keySet()) {
            maxHeap.offer(Pair.of(value, map.get(value)));
        }
        for (int i = 0; i < k; i++) {
            res[i] = maxHeap.poll().getFirst();
        }
        return res;
    }


    public int[] topKFrequency(int[] nums, int k) {
        if (nums == null || nums.length < k) {
            return new int[0];
        }
        Map<Integer, Integer> counterMap = new HashMap<>();
        for (int num : nums) {
            counterMap.put(num, counterMap.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue));
        for (var entry : counterMap.entrySet()) {
            if (minHeap.size() < k) {
                minHeap.offer(entry);
            } else {
                var minElement = minHeap.peek();
                if (entry.getValue() > minElement.getValue()) {
                    minHeap.poll();
                    minHeap.offer(entry);
                }
            }
        }
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll().getKey();
        }
        return result;
    }
}

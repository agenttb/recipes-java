package com.bintian.learn.tire;

import java.util.PriorityQueue;

public class TopkListNode {
    private static class ListNode {
        int  val;
        ListNode next;
    }
    public int getTopKNodeValue(ListNode list, int k) {
        if (list == null || k < 1) {
            return -1;
        }
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> a - b);

        ListNode p = list;
        while (p != null) {
            maxHeap.offer(p.val);
            if (maxHeap.size() > k) {
                maxHeap.poll(); 
            }
            p = p.next;
        }
        return maxHeap.poll();
    }
}

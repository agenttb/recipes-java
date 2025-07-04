package com.bintian.learn.algorithm;

import java.util.*;

public class ListSolution {
    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }
    public static void main(String[] args) {
        Deque<Integer> deque = new LinkedList<>();
       int[] array = {1,2,3,4,5};
       for (int num : array) {
           deque.offerFirst(num);
       }
       while (!deque.isEmpty()) {
           System.out.println(deque.pollFirst());
       }

//        ListNode head= new ListNode(1);
//        ListNode p = head;
//        for (int i = 1; i < array.length; i++) {
//            p.next = new ListNode(array[i]);
//            p = p.next;
//        }
//        ListSolution solution = new ListSolution();
//        solution.printList(head);
//        ListNode listNode = solution.reverseKGroup(head, 2);
//        solution.printList(listNode);
    }
    private void printList(ListNode head) {
        List<String> list = new ArrayList<>();
        ListNode p = head;
        while (p != null) {
            list.add(String.valueOf(p.val));
            p = p.next;
        }
        String res = String.join(" --> ", list);
        System.out.println(res);
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) {
            return head;
        }
        ListNode finalizeHead = null;
        int counter = 0;
        ListNode first = head;
        ListNode last = head;
        ListNode nextGroupHead = null;
        ListNode preLast = null;
        while (last != null) {
            counter++;
            if (counter % k == 0) {
                nextGroupHead = last.next;
                last.next = null;
                ListNode newHead = reverseList(first);
                if (finalizeHead == null) {
                    finalizeHead = newHead;
                    if (preLast == null) {
                        preLast = first;
                    }
                } else {
                    preLast.next = newHead;
                    preLast = newHead;
                }

                first.next = nextGroupHead;
                first = nextGroupHead;
                last = nextGroupHead;
            } else {
                last = last.next;
            }
        }
        return finalizeHead;

    }

    private ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}

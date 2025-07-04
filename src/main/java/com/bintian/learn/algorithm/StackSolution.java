package com.bintian.learn.algorithm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class StackSolution {
    public boolean validateBookSequences(int[] putIn, int[] takeOut) {
        int indexTakeOut = 0;
        int size = putIn.length;

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            stack.push(putIn[i]);
            Integer out = stack.peek();
            while (out == takeOut[indexTakeOut] ) {
                stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                out = stack.peek();
                indexTakeOut++;
            }

        }
        return indexTakeOut == size;
    }

    public static void main(String[] args) {
        int[] putIn = {6,7,8,9,10,11};
        int[] takeOut = {9,11,10,8,7,6};
        Deque<Integer> deque = new LinkedList<>();
        for (int num : putIn) {
            deque.offerFirst(num);
        }
        while (!deque.isEmpty()) {
            System.out.println(deque.pollFirst());
        }
    }
}

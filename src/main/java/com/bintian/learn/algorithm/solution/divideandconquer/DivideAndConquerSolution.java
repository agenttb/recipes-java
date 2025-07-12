package com.bintian.learn.algorithm.solution.divideandconquer;

import java.util.ArrayList;
import java.util.List;

public class DivideAndConquerSolution {
    public int reversePairs(int[] record) {
        if (record == null || record.length < 2) {
            return 0;
        }
        // 创建一个临时数组，用于归并排序，避免在递归中反复创建
        int[] temp = new int[record.length];
        return mergeSortAndCount(record, 0, record.length - 1, temp);
    }

    private int mergeSortAndCount(int[] arr, int left, int right, int[] temp) {
        if (left >= right) {
            return 0;
        }
        int mid = left + (right - left)/2;
        int leftInversions = mergeSortAndCount(arr, left, mid, temp);
        int rightInversions = mergeSortAndCount(arr, mid+1, right, temp);

        if (arr[mid] < arr[mid+1]) {
            return leftInversions + rightInversions;
        }

        int crossInversions = mergeAndCount(arr, left, mid, right, temp);

        return leftInversions + rightInversions + crossInversions;
    }

    private int mergeAndCount(int[] arr, int left, int mid, int right, int[] temp) {
        if (right + 1 - left >= 0) {
            System.arraycopy(arr, left, temp, left, right + 1 - left);
        }

        int i = left;
        int j = mid+1;
        int k = left;
        int count = 0;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                count += (mid - i + 1);
                arr[k++] = temp[j++];
            }
        }
        while (i <= mid) {
            arr[k++] = temp[i++];
        }

        while (j <= right) {
            arr[k++] = temp[j++];
        }
        return count;
    }

    public List<Integer> diffWaysToCompute(String expression) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*') {
                String leftExpr = expression.substring(0, i);
                String rightExpr = expression.substring(i + 1);

                List<Integer> leftlist = diffWaysToCompute(leftExpr);
                List<Integer> rightList = diffWaysToCompute(rightExpr);

                for (int left : leftlist) {
                    for (int right : rightList) {
                        int val = switch (c) {
                            case '+' -> left + right;
                            case '-' -> left - right;
                            case '*' -> left * right;
                            default -> 0;
                        };
                        result.add(val);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            result.add(Integer.parseInt(expression));
        }
        return result;
    }


    public static void main(String[] args) {
        DivideAndConquerSolution solution = new DivideAndConquerSolution();
        int[] record = new int[] {7,5,6,4};
        int count = solution.reversePairs(record);
        System.out.println(count);

        int[] record1 = new int[] {1,3,2,3,1};
        int count1 = solution.reversePairs(record1);
        System.out.println(count1);

        var res1 = solution.diffWaysToCompute("2-1-1");
        System.out.println(res1);
        var res2 = solution.diffWaysToCompute("2*3-4*5");
        System.out.println(res2);
    }
}

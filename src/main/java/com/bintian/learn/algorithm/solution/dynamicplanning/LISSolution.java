package com.bintian.learn.algorithm.solution.dynamicplanning;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LISSolution {

    public int[] findLexicographicallySmallestLIS(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;

        // --- Step 1: Calculate len array and max_len ---
        // len[i] = length of LIS ending at arr[i]
        int[] len = new int[n];
        // min_end[k] = the smallest end element of all increasing subsequences of length k+1
        List<Integer> min_end = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int num = arr[i];
            int index = Collections.binarySearch(min_end, num);
            if (index < 0) {
                index = -index - 1;
            }

            if (index == min_end.size()) {
                min_end.add(num);
            } else {
                min_end.set(index, num);
            }
            len[i] = index + 1;
        }

        int max_len = min_end.size();
        if (max_len == 0) {
            return new int[0];
        }

        // --- Step 2: Group elements by their LIS length ---
        ArrayList<Integer>[] groups = new ArrayList[max_len + 1];
        for(int i = 1; i <= max_len; i++) {
            groups[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            groups[len[i]].add(arr[i]);
        }

        // --- Step 3: Reconstruct the lexicographically smallest LIS ---
        int[] result = new int[max_len];
        long last_val = Long.MAX_VALUE; // Use long to avoid overflow with Integer.MAX_VALUE

        for (int k = max_len; k >= 1; k--) {
            int min_candidate = Integer.MAX_VALUE;
            // Find the smallest value in the current group that is less than last_val
            for (int val : groups[k]) {
                if (val < last_val) {
                    min_candidate = Math.min(min_candidate, val);
                }
            }
            result[k - 1] = min_candidate;
            last_val = min_candidate;
        }

        return result;
    }

    public int[] LIS (int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] len = new int[n];
        List<Integer> tails = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int index = Collections.binarySearch(tails, arr[i]);
            if (index < 0) {
                index = -index - 1;
            }
            if (index == tails.size()) {
                tails.add(arr[i]);
            } else {
                tails.set(index, arr[i]);
            }
            len[i] = index+1;
        }
        if (tails.isEmpty()) {
            return new int[0];
        }
        int maxLen = tails.size();
        List<int[]>[] groups = new ArrayList[maxLen+1];
        for (int i = 0; i < maxLen+1; i++) {
            groups[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            groups[len[i]].add(new int[]{arr[i], i});
        }

        int[] result = new int[maxLen];
        long last_val = Long.MAX_VALUE;
        int last_idx = n;

        for (int k = maxLen; k >= 1; k--) {
            int min_candidate_val = Integer.MAX_VALUE;
            int min_candidate_idx = -1;

            // Find the best candidate for this length k
            for (int[] pair : groups[k]) {
                int val = pair[0];
                int idx = pair[1];

                if (val < last_val && idx < last_idx) {
                    // This is a valid candidate. Is it the best one?
                    // We want the one with the smallest value.
                    if (val < min_candidate_val) {
                        min_candidate_val = val;
                        min_candidate_idx = idx;
                    }
                }
            }

            result[k - 1] = min_candidate_val;
            last_val = min_candidate_val;
            last_idx = min_candidate_idx;
        }
        return result;
    }

    public int pickingNumbers(List<Integer> a) {
        // Write your code here
        int[] frequency = new int[101];
        for (Integer num : a) {
            frequency[num]++;
        }
        int maxLength = 0;
        for (int i = 0; i < frequency.length-1; i++) {
            int currentLength = frequency[i] + frequency[i+1];
            maxLength = Math.max(maxLength, currentLength);
        }
        return maxLength;

    }

    public String findLCS(String text1, String text2) {
        if (text1 == null || text1.isEmpty() || text2 == null || text2.isEmpty()) {
            return "";
        }
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                char ch1 = text1.charAt(i);
                char ch2 = text2.charAt(j);
                if (ch1 == ch2) {
                    dp[i][j] = dp[i-1][j-1]+1;
                } else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        int maxLen = dp[m][n];
        int i = m;
        int j = n;
        StringBuilder lcsResult = new StringBuilder();
        while (i > 0 & j > 0) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                lcsResult.append(text1.charAt(i-1));
                i--;
                j--;
            } else if (dp[i-1][j] > dp[i][j-1]) {
                i--;
            } else {
                j--;
            }
        }
        return lcsResult.toString();

    }

    public Pair<Integer , String> matrixChainOrder(int[] vector, int n) {
        int[][] dp = new int[n+1][n+1];
        int[][] s = new int[n+1][n+1];

        for (int l = 2; l <= n; l++) {
            for (int i = 1; i <= n-l+1; i++) {
                int j = i + l -1;
                dp[i][j] = Integer.MAX_VALUE;

                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k+1][j] + vector[i-1]*vector[k]*vector[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        int minCost = dp[1][n];
        String optimalOrder = getOptimalParenthesization(1, n, s);


        return Pair.of(minCost, optimalOrder);
    }

    private String getOptimalParenthesization(int i, int j, int[][] s) {
        if (i == j) {
            return "A" + i;
        }

        String left = getOptimalParenthesization(i, s[i][j], s);
        String right = getOptimalParenthesization(s[i][j]+1, j, s);
        return "(" + left + right + ")";
    }




    public static void main(String[] args) {
        LISSolution solution = new LISSolution();
//        int[] lisArr = new int[]{2, 1, 5, 3, 6, 4, 8, 9, 7};
////        int[] lis = solution.LIS(lisArr);
////        System.out.println(Arrays.toString(lis));
//        int[] res1 = solution.LIS(lisArr);
//        System.out.println(Arrays.toString(res1));

//        int[] soredArr = new int[] {1,2,4,5,6,8,9};
//        int index = Arrays.binarySearch(soredArr, 7);
//        System.out.println(index);
//
//        List<Integer> list = new ArrayList<>();
//        for (int i : soredArr) {
//            list.add(i);
//        }
//        System.out.println(Collections.binarySearch(list, 7));
//
//        System.out.println(Collections.binarySearch(new ArrayList<>(), 7));

        Pair<Integer, String> integerStringPair = solution.matrixChainOrder(new int[]{10, 100, 5, 50}, 3);
        System.out.println(integerStringPair.getFirst());
        System.out.println(integerStringPair.getSecond());
    }
}
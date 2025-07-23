package com.bintian.learn.algorithm.solution.dynamicplanning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

public class DPSolution {
    public int crackNumber(int ciphertext) {
        String text = String.valueOf(ciphertext);
        int n = text.length();

        int fn = 0;
        int fn1 = 1;
        int fn2 = 1;
        for (int i =2; i <= n; i++) {
            fn = fn1;
            char prevChar = text.charAt(i-2);
            if (prevChar != '0') {
                    int i1 = Integer.parseInt(text.substring(i-2, i));
                    if (i1 >=10 && i1<=25) {
                        fn += fn2;
                    }
            }
            fn2 = fn1;
            fn1 = fn;
        }
        return  fn;

    }

    public int numDecodings(int ciphertext) {
        String s = String.valueOf(ciphertext);
        int n = s.length();
        if (n == 0 || s.charAt(0) == '0') return 0;

        // dp[i] represents the number of ways to decode the first i characters
        int[] dp = new int[n + 1];
        dp[0] = 1; // Base case: empty string has one way to decode
        dp[1] = 1; // First character can be decoded in one way if it's not '0'

        for (int i = 2; i <= n; i++) {
            // Check if single digit decode is valid
            if (s.charAt(i - 1) != '0') {
                dp[i] += dp[i - 1];
            }

            // Check if two digit decode is valid
            int twoDigit = Integer.parseInt(s.substring(i - 2, i));
            if (twoDigit >= 10 && twoDigit <= 25) {
                dp[i] += dp[i - 2];
            }
        }

        return dp[n];
    }

    public long cuttingBamboo(int bamboo_len) {

        if (bamboo_len <= 3) {
            return bamboo_len -1;
        }
        long[] dp = new long[bamboo_len+1];
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 3;
        for (int i = 4; i <= bamboo_len; i++) {
            long maxProd = 0;
            for (int j = 1; j <= i/2; j++) {
                long currentProd = dp[j] * dp[i -j];
                maxProd = Math.max(currentProd, maxProd);
            }
            dp[i] = maxProd;
        }
        return dp[bamboo_len];
    }

    public int minMoney (int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return -1;
        }
        if (aim == 0) {
            return 0;
        }
        int[] dp = new int[arr.length+1];

        int maxValue = aim +1;
        Arrays.fill(dp, maxValue);
        dp[0] = 0;
        for (int i = 1; i <= aim; i++) {
            for (int money : arr) {
                if (i >= money && dp[i - money] != maxValue) {
                    dp[i] = Math.min(dp[i], dp[i-money] + 1);
                }
            }
        }
        return dp[aim] == maxValue ? - 1 : dp[aim];
    }

    public boolean canPartition(int[] nums) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int n = nums.length;
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // 1. 如果总和是奇数，不可能平分，直接返回false
        if (totalSum % 2 != 0) {
            return false;
        }

        int target = totalSum / 2;

        // dp[j] 表示是否存在一个子集，其和为 j
        boolean[] dp = new boolean[target + 1];

        // Base Case: dp[0] = true (和为0的子集是空集)
        dp[0] = true;

        // 遍历物品（数组中的数字）
        for (int num : nums) {
            // 遍历背包容量（目标和）
            // 必须从后往前遍历，以保证每个物品只被使用一次
            for (int j = target; j >= num; j--) {
                // 对于每个数字num，如果我们可以凑成 j-num，
                // 那么我们现在也可以凑成 j (通过加上num)
                // dp[j] = (不放num时能否凑成j) || (放num时能否凑成j)
                // dp[j] = dp[j] || dp[j - num];

                dp[j] = dp[j] || dp[j - num];
            }
        }

        return dp[target];
    }

    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        int globalMax = nums[0];
        int currentMax = nums[0];
        int currentMin = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int tempMax = currentMax;
            currentMax = Math.max(nums[i], Math.max(tempMax*nums[i], currentMin*nums[i]));
            currentMin = Math.min(nums[i], Math.min(tempMax*nums[i], currentMin*nums[i]));
            if (currentMax > globalMax) {
                globalMax = currentMax;
            }
        }

        return globalMax;
    }

    public int designerPdfViewer(List<Integer> h, String word) {
        if (h == null || h.isEmpty() || word == null || word.isEmpty()) {
            return 0;
        }
        Integer[] hArr = new Integer[h.size()];
        Integer[] array = h.toArray(hArr);
        int maxHeight = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            maxHeight = Math.max(maxHeight, array[c - 'a']);
        }
        return word.length() * maxHeight;

    }

    public int utopianTree(int n) {
        // Write your code here
        int currentHeight = 0;
        for (int i = 0; i <= n; i++) {
            if ((i & 1) != 0) {
                currentHeight = currentHeight*2;
            } else {
                currentHeight = currentHeight + 1;
            }
        }
        return currentHeight;

    }

    public  int formingMagicSquare(List<List<Integer>> s) {
        if (s.isEmpty()) {
            return 0;
        }
        int[][][] allMagicSquares = {
                {{8, 1, 6}, {3, 5, 7}, {4, 9, 2}},
                {{6, 1, 8}, {7, 5, 3}, {2, 9, 4}},
                {{4, 9, 2}, {3, 5, 7}, {8, 1, 6}},
                {{2, 9, 4}, {7, 5, 3}, {6, 1, 8}},
                {{8, 3, 4}, {1, 5, 9}, {6, 7, 2}},
                {{4, 3, 8}, {9, 5, 1}, {2, 7, 6}},
                {{6, 7, 2}, {1, 5, 9}, {8, 3, 4}},
                {{2, 7, 6}, {9, 5, 1}, {4, 3, 8}}
        };
        int minCost = Integer.MAX_VALUE;
        for (int[][] magicSquares : allMagicSquares) {
            int currentCost = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    currentCost += Math.abs(s.get(i).get(j) - magicSquares[i][j]);
                }
            }
            minCost = Math.min(currentCost, minCost);
        }
        return minCost;

    }

    public int hurdleRace(int k, List<Integer> list) {
        int magicPotionCnt = 0;
        for (Integer height : list) {
            if (k < height) {
                magicPotionCnt = Math.max(magicPotionCnt, height - k);
            }
        }
        return magicPotionCnt;
    }

    public static void main(String[] args) {
        var solution = new DPSolution();
//        int i = solution.crackNumber(216612);
//        System.out.println(i);
//
//        int i1 = solution.numDecodings(216612);
//        System.out.println(i1);
//
//        long i2 = solution.cuttingBamboo(120);
//        System.out.println(i2);
//        var res = solution.maxProduct(new int[] {-2, 3, -4});
//        System.out.println(res);

//        var res1 = solution.designerPdfViewer(
//                Arrays.asList(1 ,3 ,1 ,3 ,1 ,4 ,1 ,3 ,2 ,5 ,5 ,5, 5, 5, 5 ,5 ,5, 5 ,5 ,5 ,5 ,5 ,5, 5 ,5 ,7), "zaba");
//        System.out.println(res1);
//        int[] inputs= new int[] {0, 1, 4};
//        for (int input : inputs) {
//            var res1 = solution.utopianTree(input);
//            System.out.println(res1);
//        }
        List<List<Integer>> list = new ArrayList<>();
        list.add(Arrays.asList(5,3,4));
        list.add(Arrays.asList(1, 5, 8));
        list.add(Arrays.asList(6, 4, 2));
        var res1 = solution.formingMagicSquare(list);
        System.out.println(res1);

        List<List<Integer>> list2 = new ArrayList<>();
        list2.add(Arrays.asList(4,8,2));
        list2.add(Arrays.asList(4, 5, 7));
        list2.add(Arrays.asList(6, 1, 6));
        var res2 = solution.formingMagicSquare(list2);
        System.out.println(res2);

        List<List<Integer>> list3 = new ArrayList<>();
        list3.add(Arrays.asList(4,9,2));
        list3.add(Arrays.asList(3, 5, 7));
        list3.add(Arrays.asList(8, 1, 5));
        var res3 = solution.formingMagicSquare(list3);
        System.out.println(res3);



    }
}

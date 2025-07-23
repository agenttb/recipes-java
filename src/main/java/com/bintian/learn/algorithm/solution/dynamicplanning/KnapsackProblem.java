package com.bintian.learn.algorithm.solution.dynamicplanning;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnapsackProblem {
    // weights[i]: represents the weight of i-th item;
    // values[i[: represents the value of i-th item;
    // n : represents the number of item;
    // capacity : represents the capacity of knapsack

    public static record KnapsackResult (int maxValue, List<Integer> knapsackList){

    };

    public KnapsackResult zeroOneSolve(int n, int[] weights, int[] values, int capacity) {
        if (weights == null || values == null || weights.length == 0 || values.length == 0) {
            return new KnapsackResult(0, new ArrayList<>());
        }
        int[][] dp = new int[n+1][capacity+1];
        //int[] dp = new int[capacity+1];  dp[i] only dependent dp[i-1], so can optimize for dp[]
        for (int i = 1; i <= n; i++) {
            // one item only put 1 or 0  times, so traverse from back to front
            for (int j = capacity; j > weights[i]; j--) {
                // dp[j] = Math.max(dp[j], dp[j - weights[i]] + values[i]);
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j - weights[i]] + values[i]);
            }
        }
        int maxValue = dp[n][capacity]; // dp[capacity]
        return new KnapsackResult(maxValue, new ArrayList<>());
    }

    public KnapsackResult completeSolve(int n, int[] weights, int[] values, int capacity) {
        if (weights == null || values == null || weights.length == 0 || values.length == 0) {
            return new KnapsackResult(0, new ArrayList<>());
        }
        int[][] dp = new int[n+1][capacity+1];
        //int[] dp = new int[capacity+1];  dp[i] only dependent dp[i-1], so can optimize for dp[]
        for (int i = 1; i <= n; i++) {
            // one item can put any times
            for (int j = weights[i]; j <= capacity; j++) {
                // dp[j] = Math.max(dp[j], dp[j - weights[i]] + values[i]);
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j - weights[i]] + values[i]);
            }
        }
        int maxValue = dp[n][capacity];
        return new KnapsackResult(maxValue, new ArrayList<>());
    }

    public KnapsackResult mutiDimensionSolve(int[] weights,
                                             int[] volumes,
                                             int[] values,
                                             int volumeCapacity,
                                             int weightCapacity) {
        if (weights == null || volumes == null || values == null ||
                weights.length != volumes.length || weights.length != values.length) {
            return new KnapsackResult(0, new ArrayList<>());
        }

        int[][] dp = new int[weightCapacity+1][volumeCapacity+1];
        for (int i = 1; i <=weights.length; i++) {
            for (int j = weights[i]; j < weightCapacity; j++) {
                for (int k = volumes[i]; k < volumeCapacity; k++) {
                    dp[j][k] = Math.max(dp[j][k], dp[j-weights[i]][k-values[i]] + values[i]);
                }
            }
        }
        int maxValue = dp[weightCapacity+1][volumeCapacity+1];
        return new KnapsackResult(maxValue, new ArrayList<>());
    }

    /**
     *
     * @param amount
     * @param coins
     * @return the minimum number of coin for target amount
     */
    public int coinsSolve(int amount, int[] coins) {
        if (coins == null || coins.length == 0) {
            return -1;
        }
        int[] dp = new int[amount+1];
        Arrays.fill(dp, amount+1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}

package com.bintian.learn.algorithm.solution.list;

import java.util.*;

public class ListSolution {

    public List<Integer> climbingLeaderBoard(List<Integer> ranked, List<Integer> player) {
        if (ranked == null || ranked.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> uniqueRanked = new ArrayList<>(new LinkedHashSet<>(ranked));
        List<Integer> result= new ArrayList<>();
        int rankPointer = uniqueRanked.size() -1;

        for (int playerScore : player) {
            while (rankPointer >=0 && playerScore >=uniqueRanked.get(rankPointer)) {
                rankPointer--;
            }
            int playerRank = rankPointer + 2;
            result.add(playerRank);
        }

        return result;
    }


    public static void main(String[] args) {
        ListSolution solution = new ListSolution();
        var test1 = solution.climbingLeaderBoard(List.of(100, 100 ,50, 40 ,40 ,20 ,10), List.of(4, 5 ,25, 50, 120));
        test1.forEach(v -> System.out.print(v + " "));
        System.out.println();

        var test2 = solution.climbingLeaderBoard(List.of(100 ,90 ,90 ,80, 75, 60), List.of(50 ,65 ,77, 90 ,102));
        test2.forEach(v -> System.out.print(v + " "));
        System.out.println();

        List<Integer> test3 = solution.climbingLeaderBoard(List.of(100 ,90 ,90 ,80), List.of(70, 80, 105));
        test3.forEach(v -> System.out.print(v + " "));
        System.out.println();
        test3.stream().toArray();
        test3.toArray();
    }
}

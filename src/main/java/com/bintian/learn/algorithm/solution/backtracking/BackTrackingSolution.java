package com.bintian.learn.algorithm.solution.backtracking;

import java.util.*;
import java.util.stream.Collectors;

public class BackTrackingSolution {
    private final String[] nbrMap = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private void backtrack(String digits, int index, StringBuilder path, List<String> result) {
        if (index == digits.length()) {
            result.add(path.toString());
            return;
        }
        String letter = nbrMap[Character.getNumericValue(digits.charAt(index))];
        for (char c : letter.toCharArray()) {
            path.append(c);
            backtrack(digits, index + 1, path, result);
            path.deleteCharAt(path.length() - 1);
        }
    }

    public List<String> bfs(String digits) {
        Queue<String> queue = new LinkedList<>();
        queue.offer("");
        List<String> result = new ArrayList<>();

        for (char num : digits.toCharArray()) {
            String letters = nbrMap[Character.getNumericValue(num)];
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                String current = queue.poll();
                for (char c : letters.toCharArray()) {
                    queue.offer(current + c);
                }
            }
        }
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;
    }

    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() <4) {
            return result;
        }
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(String s, int start, List<String> path, List<String> result) {
        if (path.size() == 4 && start == s.length()) {
            result.add(String.join(".", path));
            return;
        }
        for (int i = 1; i<=3; i++) {
            if (start + i > s.length()) break;
            String segment = s.substring(start, start+i);
            if (!isValidIpSegment(segment)) continue;
            path.add(segment);
            backtrack(s, start+i, path, result);
            path.removeLast();
        }
    }

    private boolean isValidIpSegment(String ip) {

        if (ip.startsWith("0") && ip.length() > 1) {
            return false;
        }
        try {
            int digit = Integer.parseInt(ip);
            if (digit < 0 || digit > 255) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }


        return true;
    }

    public boolean exist(char[][] board, String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        int m = board.length;
        int n = board[0].length;
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (backtrack(board, x, y, word, 0, m, n)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean backtrack(char[][] board, int x, int y, String word, int index, int m, int n) {
        if (index == word.length()) {
            return true;
        }
        if (x <0 || x >=m || y < 0 || y >= n) {
            return false;
        }
        if (board[x][y] != word.charAt(index)) {
            return false;
        }

        char temp = board[x][y];
        board[x][y] = '#';
        boolean found =
                backtrack(board, x-1 , y, word, index+1, m, n)
                || backtrack(board, x+1, y, word, index+1, m, n)
                || backtrack(board, x, y-1, word, index+1, m, n)
                || backtrack(board, x, y+1, word, index+1, m, n);
        board[x][y] = temp;
        return found;
    }

    public List<List<Character>> permuteUnique(char[] nums) {
        List<List<Character>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        Arrays.sort(nums);
        int n= nums.length;
        backtrackPermuteUnique(nums, new LinkedList<>(), result, n, new boolean[nums.length]);
        return result;
    }

    private void backtrackPermuteUnique(char[] nums, List<Character> path, List<List<Character>> result, int n, boolean[] visited) {
        if (path.size() == n) {
            return;
        }

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            if (i > 0 && nums[i] == nums[i-1] && !visited[i-1]) continue;
            path.add(nums[i]);
            visited[i] = true;
            backtrackPermuteUnique(nums, path, result, n, visited);
            path.removeLast();
            visited[i] = false;
        }


    }

    public String[] goodsOrder(String goods) {
        if (goods == null || goods.length() == 0) {
            return new String[0];
        }
        List<String> list = new ArrayList<>();
        backtrack(goods, new LinkedList<>(), list, new boolean[goods.length()]);
        return list.toArray(new String[list.size()]);
    }
    private void backtrack(String goods, List<Character> path, List<String> result, boolean[] visited) {
        if (path.size() == goods.length()) {
            result.add(path.stream().map(String::valueOf).collect(Collectors.joining()));
            return;
        }
        for (int i = 0; i < goods.length(); i++) {
            if (visited[i]) continue;
            if (i > 0 && goods.charAt(i) == goods.charAt(i-1) && !visited[i-1]) continue;
            path.add(goods.charAt(i));
            visited[i] = true;
            backtrack(goods, path, result, visited);
            path.removeLast();
            visited[i] = false;
        }
    }

    public List<List<Integer>> combinationSum3(int k, int n) {
        if (k < 1) {
            return new ArrayList<>();
        }
        int[] candidates = new int[9];
        for (int i = 0; i < 9; i++) {
            candidates[i] = i+1;
        }
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        backTrackCombinationSum3(candidates, 0, n, new LinkedList<>(), result, k);
        return result;
    }

    void backTrackCombinationSum3(int[] candidates, int start, int target, List<Integer> path, List<List<Integer>> result, int k) {
        if (target == 0 && path.size() == k) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (path.size() > k) {
            return;
        }
        if (target < 0) {
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            path.add(candidates[i]);
            backTrackCombinationSum3(candidates, i + 1, target - candidates[i], path, result, k);
            path.removeLast();
        }
    }

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtracking(nums, 0, new LinkedList<>(), result, visited);
        return result;
    }

    void backtracking(int[] nums, int start, List<Integer> path, List<List<Integer>> list, boolean[] visited) {

        list.add(new ArrayList<>(path));

        for (int i  = start; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i-1] && !visited[i-1]) continue;

            path.add(nums[i]);
            visited[i] = true;
            backtracking(nums, i+1, path, list, visited);
            path.removeLast();
            visited[i] = false;
        }
    }

    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return result;
        }
        backtrackForPartition(s, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrackForPartition(String s, int start, List<String> path, List<List<String>> result) {
        if (start >= s.length()) {
            result.add(new ArrayList<>(path));
        }
        for (int i = 1; i<= s.length() - start; i++) {
            if (start + i > s.length()) break;
            String segment = s.substring(start, start+i);
            if (!isPalindrome(segment)) continue;
            path.add(segment);
            backtrackForPartition(s, start+i, path, result);
            path.removeLast();
        }
    }

    private boolean isPalindrome(String s) {
        int l = 0;
        int r = s.length() -1;
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--)) {
                return false;
            }
        }
        return true;
    }

    private final Set<String> resultSet = new HashSet<>();
    private String originalString;
    private int len;
    public List<String> removeInvalidParentheses(String s) {
        this.originalString = s;
        this.len = s.length();

        int leftToRemove = 0;
        int rightToRemove = 0;
        for (int i = 0;  i < len; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                leftToRemove++;
            } else if (c == ')') {
                if (leftToRemove > 0) {
                    leftToRemove--;
                } else {
                    rightToRemove++;
                }
            }
        }
        backtrackForRIP(0,0,0, leftToRemove, rightToRemove, new StringBuilder());

        return new ArrayList<>(resultSet);
    }

    void backtrackForRIP(int index, int leftCount, int rightCount, int leftToRemove, int rightToRemove, StringBuilder path) {
        if (index == len) {
            if (leftToRemove == 0 && rightToRemove == 0) {
                resultSet.add(path.toString());
            }
            return;
        }
        char currentChar = originalString.charAt(index);
        if (currentChar == '(' && leftToRemove > 0) {
            backtrackForRIP(index+1, leftCount, rightCount, leftToRemove-1, rightToRemove, path);
        } else if (currentChar == ')' && rightToRemove > 0) {
            backtrackForRIP(index+1, leftCount, rightCount, leftToRemove, rightToRemove -1 , path);
        }
        path.append(currentChar);

        if (currentChar != '(' && currentChar != ')') {
            backtrackForRIP(index+1, leftCount, rightCount, leftToRemove, rightToRemove, path);
        } else if (currentChar == '(') {
            backtrackForRIP(index+1 , leftCount+1, rightCount, leftToRemove, rightToRemove, path);
        } else if (rightCount < leftCount) {
            backtrackForRIP(index+1, leftCount, rightCount +1, leftToRemove, rightToRemove, path);
        }
        path.deleteCharAt(path.length()-1);

    }


    public static void main(String[] args) {
        BackTrackingSolution solution = new BackTrackingSolution();
//        var list = solution.letterCombinations("23");
//        printList(list);
//        var list1 = solution.letterCombinations("2");
//        printList(list1);
//        var list2 = solution.letterCombinations("234");
//        printList(list2);

//        var list = solution.bfs("23");
//        printList(list);
//        var list1 = solution.bfs("2");
//        printList(list1);
//        var list2 = solution.bfs("234");
//        printList(list2);
//        List<String> strings = solution.restoreIpAddresses("25525511135");
//        printList(strings);
//        var res1 = solution.exist(new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}}, "ABCCED");
//        System.out.println(res1);
//
//        char[][] board2 = {
//                {'A', 'B', 'C', 'E'},
//                {'S', 'F', 'C', 'S'},
//                {'A', 'D', 'E', 'E'}
//        };
//        var res2 = solution.exist(board2, "ABCB");
//        System.out.println(res2);
//        String join = String.join("->", new ArrayList<>());
//        var combinationSum3 = solution.combinationSum3(3, 7);
//        printList(combinationSum3);
//        var combinationSum4 = solution.combinationSum3(3, 9);
//        var subset = solution.partition("aab");
//        printList(subset);
//
//        var p2 = solution.partition("a");
//        printList(p2);
//        var list = solution.removeInvalidParentheses("()())()");
//        printList(list);
        var list1= solution.removeInvalidParentheses("(a)())()");
        printList(list1);

        var a = new int[] {0};

    }

    private static <T> void printList(List<T> list) {
        if (list.isEmpty()) {
            System.out.println("[]");
        }
        list.forEach(v -> System.out.print(v + " "));
        System.out.println();
    }
}

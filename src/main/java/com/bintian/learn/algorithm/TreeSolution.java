package com.bintian.learn.algorithm;

import java.util.*;

public class TreeSolution {
    private int max;
    private boolean isBalance = false;
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        max = Math.max(max, leftDepth + rightDepth);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = minDepth(root.left);
        int rightDepth = minDepth(root.right);
        if (leftDepth == 0 || rightDepth == 0) {
            return leftDepth + rightDepth + 1;
        }
        return 1 + Math.min(leftDepth, rightDepth);
    }

    public int countNodeByDFS(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return countNodeByDFS(node.left) + countNodeByDFS(node.right) + 1;

    }

    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = getDepth(root.left);
        int rightDepth = getDepth(root.right);
        if (leftDepth == rightDepth) {
            return (int)Math.pow(2, leftDepth) + countNodes(root.right);
        } else {
            return (int)Math.pow(2, rightDepth) + countNodes(root.left);
        }
    }

    /**
     * Just for complete binary tree
     * @param root
     * @return
     */
    private int getDepth(TreeNode root) {
        int depth = 0;
        TreeNode node = root;
        while (node != null) {
            depth ++;
            node = node.left;
        }
        return depth;
    }

    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        maxDepth(root);
        return max;
    }

    public boolean isBalanceTree(TreeNode root) {
        maxDepthForBalanceTree(root);
        return isBalance;
    }

    private int maxDepthForBalanceTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = maxDepthForBalanceTree(root.left);
        int right = maxDepthForBalanceTree(root.right);
        if (Math.abs(left - right) <= 1) {
            isBalance = true;
        }
        return 1 + Math.max(left, right);
    }

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSymmetric(root.left, root.right);
    }

    public boolean isSymmetric(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 == null || t2 == null) {
            return false;
        }
        if (t1.val != t2.val) {
            return false;
        }
        return isSymmetric(t1.left, t2.right) && isSymmetric(t1.right, t2.left);
    }
    private int currentVal = Integer.MIN_VALUE;
    public boolean middle(TreeNode t) {
        if (t == null)
            return true;
        if (!middle(t.left))
            return false;
        if (t.val <= currentVal)
            return false;
        currentVal = t.val;
        if (!middle(t.right))
            return false;
        return true;
    }

    public boolean isCompleteTree(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (queue.peek() != null) {
            TreeNode node = queue.poll();
            queue.add(node.left);
            queue.add(node.right);
        }
        while (!queue.isEmpty() && queue.peek() == null) {
            queue.poll();
        }
        return queue.isEmpty();
    }

    public boolean isSubTree(TreeNode t1, TreeNode t2) {

        return (t1 != null && t2 != null) && recur(t1, t2) || isSubTree(t1.left, t2) || isSubTree(t1.right, t2);
    }

    public boolean recur(TreeNode t1, TreeNode t2) {
        if (t2 == null) {
            return false;
        }
        if (t1 != null && t1.val != t2.val) {
            return false;
        }
        return recur(t1.left, t2.left) && recur(t1.right, t2.right);
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        String[] arr = {"apple", "banana", "orange"};

        String result = String.join(", ", arr);
        result.split("\\.");
        System.out.println(result);
    }

    // 首先判断 p q的值和根节点的大小
    // if roo is common ancestor for p and q, then if p < q  then  p < root.val < q, if q > p then q < root.val < p
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        if (root.val >= p.val && root.val <= q.val) {
            return root;
        }
        else if  (root.val < p.val) {
             return lowestCommonAncestor(root.right, p, q);
        } else {
            return lowestCommonAncestor(root.left, p, q);
        }
    }




}

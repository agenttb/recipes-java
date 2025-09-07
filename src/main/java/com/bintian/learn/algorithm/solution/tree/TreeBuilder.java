package com.bintian.learn.algorithm.solution.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBuilder {
    public static class  TreeNode<T> {
        private String id;
        private String parentId;
        private T data;
        private List<TreeNode<T>> children;
    }

    public TreeBuilder() {

    }

    public <T> List<TreeNode<T>> buildTree(List<TreeNode<T>> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, TreeNode<T>> nodeMap = new HashMap<>(nodes.size());
        for (var node : nodes) {
            nodeMap.put(node.id, node);
        }

        List<TreeNode<T>> rootNodes = new ArrayList<>();
        for (var node : nodes) {
            String parentId = node.parentId;
            if (parentId == null || "0".equals(parentId)) {
                rootNodes.add(node);
            } else {
                var parentNode = nodeMap.get(node.parentId);
                if (parentNode != null) {
                    parentNode.children.add(node);
                }
            }
        }
        return  rootNodes;
    }

    public <T> List<TreeNode<T>> sumValueToRoot(TreeNode<T> root) {
        if (root.children != null && !root.children.isEmpty()) {
            for (TreeNode<T> child : root.children) {
                sumValueToRoot(child);
                root.data = root.data + child.data;
            }
        }
    }
}

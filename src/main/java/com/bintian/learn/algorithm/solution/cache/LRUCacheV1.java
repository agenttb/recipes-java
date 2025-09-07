package com.bintian.learn.algorithm.solution.cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheV1<K, V> {
    private static class DLinkedListNode<K, V> {
        K key;
        V value;
        DLinkedListNode<K, V> prev;
        DLinkedListNode<K, V> next;
        public DLinkedListNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public DLinkedListNode() {

        }
    };


    private final Map<K, DLinkedListNode<K, V>> cache;
    private DLinkedListNode<K, V> head, tail;
    private int capacity;
    private int size;

    public LRUCacheV1(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.size = 0;

        this.head = new DLinkedListNode<>();
        this.tail = new DLinkedListNode<>();
        head.next = tail;
        tail.prev = head;
    }

    public void put(K key, V value) {
        var node = cache.get(key);
        if (node == null) {
            node = new DLinkedListNode<>(key, value);

            DLinkedListNode<K, V> p = head.next;
            head.next = node;
            node.prev = head;
            node.next = p;
            p.prev = node;
            cache.put(key, node);
            size++;
            if (size > capacity) {
                var removedNode = removeLastNode();
                cache.remove(removedNode.key);
                size--;
            }
        } else {
            node.value = value;
            moveToHead(node);

        }
    }

    public V get(K key) {
        var node = cache.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    public V remove(K key) {
        var node = cache.get(key);
        if (node == null) {
            return null;
        }
        node.next.prev = node.prev;
        node.prev.next = node.next;
        node.next = null;
        node.prev = null;
        var v = cache.remove(key);
        size--;
        return v.value;
    }

    private void moveToHead(DLinkedListNode<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        var p = head.next;
        tail.next = node;
        node.prev = tail;
        node.next = p;
        p.prev = node;
    }

    private DLinkedListNode<K, V> removeLastNode() {
        var removingNode = tail.prev;
        removingNode.prev.next = tail;
        tail.prev = removingNode.prev;
        removingNode.next = null;
        removingNode.prev = null;
        return removingNode;
    }

    public static void main(String[] args) {
        LRUCacheV1<Integer, Integer> cache = new LRUCacheV1<>(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));  // 返回 1

        cache.put(3, 3);    // 该操作会使得关键字 2 作废
        System.out.println(cache.get(2));  // 返回 -1 (未找到)

        cache.put(4, 4);    // 该操作会使得关键字 1 作废
        System.out.println(cache.get(1));  // 返回 -1 (未找到)
        System.out.println(cache.get(3));  // 返回 3
        System.out.println(cache.get(4));  // 返回 4


    }

}

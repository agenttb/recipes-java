package com.bintian.learn.algorithm.solution.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final Map<K, V> cache;
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V remove(K key) {
        return cache.remove(key);
    }

    public int size() {
        return cache.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var entry : cache.values()) {
            sb.append(entry).append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        System.out.println("缓存内容: " + cache); // {1=A, 2=B, 3=C}

        cache.get(1); // 访问1，使其成为最近使用的
        System.out.println("访问1后: " + cache); // {2=B, 3=C, 1=A}

        cache.put(4, "D"); // 超出容量，淘汰最久未使用的2
        System.out.println("添加4后: " + cache); // {3=C, 1=A, 4=D}

        cache.put(3, "C+"); // 更新3
        System.out.println("更新3后: " + cache); // {1=A, 4=D, 3=C+}
    }


}

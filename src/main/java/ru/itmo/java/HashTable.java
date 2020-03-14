package ru.itmo.java;

import java.util.Arrays;

public class HashTable {

    private final int INITIAL_CAPACITY = 1024;
    private final float LOAD_FACTOR = 0.5f;
    private int threshold;

    private class Entry {

        Object key, value;

        public Entry(Object k, Object v) {
            this.key = k;
            this.value = v;
        }
    }

    private int size = 0;
    private final float loadFactor;
    private Entry[] Dict;
    private Boolean[] Deleted;

    public HashTable() {
        loadFactor = LOAD_FACTOR;
        Dict = new Entry[INITIAL_CAPACITY];
        this.threshold = (int) (this.Dict.length * this.loadFactor);
        Deleted = new Boolean[INITIAL_CAPACITY];
        Arrays.fill(Deleted, false);

    }

    public HashTable(int initialCapacity) {
        loadFactor = LOAD_FACTOR;
        Dict = new Entry[initialCapacity];
        Deleted = new Boolean[initialCapacity];
        this.threshold = (int) (this.Dict.length * this.loadFactor);
        Arrays.fill(Deleted, false);

    }

    public HashTable(int initialCapacity, float lF) {
        this.loadFactor = lF;
        Dict = new Entry[initialCapacity];
        Deleted = new Boolean[initialCapacity];
        this.threshold = (int) (this.Dict.length * this.loadFactor);
        Arrays.fill(Deleted, false);
    }

    private int resHash(Object key) {
        int hash = key.hashCode() % Dict.length;
        if (hash < 0) {
            hash += Dict.length;
        }
        return hash;
    }

    Object put(Object key, Object value) {
        //find to replace
        int hash = resHash(key);
        while (Dict[hash] != null) {
            if (key.equals(Dict[hash].key)) {
                Object exValue = Dict[hash].value;
                Dict[hash].value = value;
                return exValue;
            }
            hash++;
            if (hash == Dict.length) {
                hash = 0;
            }
        }
        //find to put new
        hash = resHash(key);
        while (Dict[hash] != null && !Deleted[hash]) {
            hash++;
            if (hash == Dict.length) {
                hash = 0;
            }
        }

        Deleted[hash] = false;
        Dict[hash] = new Entry(key, value);
        size++;
        // Dict.length * loadfactor = threshold
        if (size > threshold) {
            Entry[] exDict = Dict;
            Dict = new Entry[exDict.length * 2];
            size = 0;
            Deleted = new Boolean[Dict.length];
            Arrays.fill(Deleted, false);
            this.threshold = (int) (this.Dict.length * this.loadFactor);

            for (Entry pair : exDict) {
                if (pair != null && pair.key != null && pair.value != null) {
                    put(pair.key, pair.value);
                }
            }
        }
        return null;
    }

    Object get(Object key) {
        int hash = resHash(key);
        while (Dict[hash] != null || Deleted[hash]) {
            if (Deleted[hash]) {
                hash++;
                if (hash == Dict.length) {
                    hash = 0;
                }
                continue;
            }
            if (key.equals(Dict[hash].key)) {
                return Dict[hash].value;
            }
            hash++;
            if (hash == Dict.length) {
                hash = 0;
            }
        }
        return null;
    }

    Object remove(Object key) {
        int hash = resHash(key);
        while (Dict[hash] != null || Deleted[hash]) {
            if (Deleted[hash]) {
                hash++;
                if (hash == Dict.length) {
                    hash = 0;
                }
                continue;
            }
            if (key.equals(Dict[hash].key)) {
                Object exValue = Dict[hash].value;
                Dict[hash].key = null;
                Dict[hash].value = null;
                Deleted[hash] = true;
                size--;
                return exValue;
            }
            hash++;
            if (hash == Dict.length) {
                hash = 0;
            }
        }
        return null;
    }

    int size() {
        return size;
    }
//
}

package ru.itmo.java;

public class HashTable {

    private final int INITIAL_CAPACITY = 1024;
    private final float LOAD_FACTOR = 0.5f;
    private final float THRESHOLD_FACTOR = 0.6f;

    private class Entry {

        Object key, value;

        public Entry(Object k, Object v) {
            this.key = k;
            this.value = v;
        }
    }

    private int size = 0;
    private int realSize = 0;
    private final float loadFactor;
    private Entry[] Dict;
    private Boolean[] Deleted;

    public HashTable() {
        loadFactor = LOAD_FACTOR;
        Dict = new Entry[INITIAL_CAPACITY];
        Deleted = new Boolean[INITIAL_CAPACITY];
        for (int i = 0; i < Deleted.length; i++) {
            Deleted[i] = false;
        }
    }

    public HashTable(int initialCapacity) {
        loadFactor = LOAD_FACTOR;
        Dict = new Entry[initialCapacity];
        Deleted = new Boolean[initialCapacity];
        for (int i = 0; i < Deleted.length; i++) {
            Deleted[i] = false;
        }
    }

    public HashTable(int initialCapacity, float lF) {
        this.loadFactor = lF;
        Dict = new Entry[initialCapacity];
        Deleted = new Boolean[initialCapacity];
        for (int i = 0; i < Deleted.length; i++) {
            Deleted[i] = false;
        }
    }

    private int resHash(Object key) {
        int hash = key.hashCode() % Dict.length;
        if (hash < 0) {
            hash += Dict.length;
        }
        return hash;
    }

    Object put(Object key, Object value) {
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

        Deleted[hash] = false;
        if (!Deleted[hash]) {
            realSize++;
        }
        Dict[hash] = new Entry(key, value);
        size++;
        // Dict.length * THRESHOLD_FACTOR = threshold
        if (realSize > Dict.length * THRESHOLD_FACTOR || size > Dict.length * loadFactor) {
            Entry[] exDict = Dict;
            Dict = new Entry[exDict.length * 2];
            size = 0;
            realSize = 0;
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

}

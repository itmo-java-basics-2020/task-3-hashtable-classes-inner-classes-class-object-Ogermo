package ru.itmo.java;

import java.util.Arrays;

public class HashTable {

    private static final int INITIAL_CAPACITY = 1024;
    private static final float LOAD_FACTOR = 0.5f;
    private int threshold;

    private int size = 0;
    private final float loadFactor;
    private Entry[] dict;
    private Boolean[] deleted;

    public HashTable() {
        this(INITIAL_CAPACITY,LOAD_FACTOR);
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity,LOAD_FACTOR);
    }

    public HashTable(int initialCapacity, float lF) {
        this.loadFactor = lF;
        dict = new Entry[initialCapacity];
        deleted = new Boolean[initialCapacity];
        this.threshold = (int) (this.dict.length * this.loadFactor);
        Arrays.fill(deleted, false);
    }

    private int resHash(Object key) {
        int hash = key.hashCode() % dict.length;
        if (hash < 0) {
            hash += dict.length;
        }
        return hash;
    }

    public Object put(Object key, Object value) {
        //find to replace
        int putNew;
        int hash = resHash(key);
        hash = FindNextEntry(hash-1);
        while (hash != -1){
            if (key.equals(dict[hash].key)) {
                Object exValue = dict[hash].value;
                dict[hash].value = value;
                return exValue;
            }
            hash = FindNextEntry(hash);
        }
        //find to put new
        hash = resHash(key);
        while (dict[hash] != null && !deleted[hash]) {
            hash++;
            if (hash == dict.length) {
                hash = 0;
            }
        }

        deleted[hash] = false;
        dict[hash] = new Entry(key, value);
        size++;
        // Dict.length * loadfactor = threshold
        if (size > threshold) {
            Entry[] exDict = dict;
            dict = new Entry[exDict.length * 2];
            size = 0;
            deleted = new Boolean[dict.length];
            Arrays.fill(deleted, false);
            this.threshold = (int) (this.dict.length * this.loadFactor);

            for (Entry pair : exDict) {
                if (pair != null) {
                    put(pair.key, pair.value);
                }
            }
        }
        return null;
    }

    Object get(Object key) {
        int hash = resHash(key);
        hash = FindNextEntry(hash - 1); //find if first hash is viable
        while (hash != -1){
            if (key.equals(dict[hash].key)) {
                return dict[hash].value;
            }
            hash = FindNextEntry(hash);
        }
        return null;
    }

    Object remove(Object key) {
        int hash = resHash(key);
        hash = FindNextEntry(hash - 1); //find if first hash is viable
        while (hash != -1){
            if (key.equals(dict[hash].key)) {
                Object exValue = dict[hash].value;
                dict[hash] = null;
                deleted[hash] = true;
                size--;
                return exValue;
            }
            hash = FindNextEntry(hash);
        }
        return null;
    }


    int FindNextEntry(int hash){
        hash++;
        if (hash == dict.length) {
            hash = 0;
        }
        while (deleted[hash]){
            hash++;
            if (hash == dict.length) {
                hash = 0;
            }
        }
        if (dict[hash] != null){
            return hash;
        }
        return -1; //next entry don't found
    }

    int size() {
        return size;
    }

    private class Entry {

        final Object key;
        Object value;

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
//
}

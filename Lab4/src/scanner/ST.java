package scanner;

import java.util.Objects;

//1a
public class ST {
    private String[] table;
    private int capacity;

    public ST(int initialCapacity){
        this.capacity = initialCapacity;
        this.table = new String[initialCapacity];
    }

    public int hash(String key,int i){
        int hash = 0;
        for (int j=0;j<key.length();j++){
            hash = hash + key.charAt(j);
        }
        return (hash + i) % capacity;
    }

    public int put(String key){
        int search = get(key);
        if (search != -1)
            return search;

        int i = 0;
        int bucket = hash(key,i);

        while (i < capacity && table[bucket] != null) {
            i++;
            bucket = hash(key,i);
        }
        if (i == capacity){
            rehash();
            return put(key);
        }
        table[bucket] = key;
        return bucket;
    }

    public void rehash(){
        String[] auxTable = table;
        table = new String[capacity*2];
        capacity *= 2;
        for (String entry: auxTable){
                this.put(entry);
        }
    }

    public int get(String key) {
        int i = 0;
        int bucket = hash(key,i);

        while (i < capacity && !Objects.equals(table[bucket], key)) {
            i++;
            bucket = hash(key,i);
        }
        if (i < capacity)
            return bucket;
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("POS").append("  ").append("SYMBOL").append("\n");
        for (int i =0;i<table.length;i++){
            if (table[i] != null)
                str.append(i).append("  ").append(table[i]).append("\n");
        }
        return str.toString();
    }
}

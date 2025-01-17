package org.example.db;

public class Pokemon {
    private final int id;
    private final String name;

    public Pokemon(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

}

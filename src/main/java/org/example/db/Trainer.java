package org.example.db;

public class Trainer {
    private final int id;
    private final String name;
    private int pokemonOwned;

    public Trainer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Trainer(int id, String name, int pokemonOwned) {
        this.id = id;
        this.name = name;
        this.pokemonOwned = pokemonOwned;
    }

    public int getPokemonOwned() {
        return pokemonOwned;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

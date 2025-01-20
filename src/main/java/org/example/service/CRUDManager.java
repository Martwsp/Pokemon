package org.example.service;

import org.example.db.DBPokemonService;
import org.example.db.Pokemon;
import org.example.db.Trainer;
import org.example.utility.InputUtils;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class CRUDManager {

    private static final Logger logger = getLogger(CRUDManager.class);

    private final DBPokemonService pokemonService;

    public CRUDManager() {
        this.pokemonService = new DBPokemonService();
    }

    public void printOptions() {
        while (true) {
            System.out.println("_______________________________________________________________");
            System.out.println("0. Get all trainers");
            System.out.println("1. Get all wild pokemon");
            System.out.println("2. Catch pokemon");
            System.out.println("3. Get all pokemon owned by a trainer");
            System.out.println("4. Get trainers ordered by the number of pokemon owned");
            System.out.println("5. Add a new trainer");
            System.out.println("6. Add a pokemon to a trainer");
            System.out.println("7. Add a new wild pokemon");
            System.out.println("8. Exit");
            System.out.println("_______________________________________________________________");

            final int choice = InputUtils.readInt();
            try {
                switch (choice) {
                    case 0 -> printTrainers();
                    case 1 -> printWild();
                    case 2 -> catchWild();
                    case 3 -> printTrainerPokemon();
                    case 4 -> printTrainerBest();
                    case 5 -> createTrainer();
                    case 6 -> createPokemonOwned();
                    case 7 -> createWild();
                    case 8 -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (ExceptionInInitializerError e) {
                logger.error("Error while connecting to database", e);
                System.out.println("Make sure that a valid database is available at port 3306");
                System.out.println("and correct username and password are specified in application.properties");
                return;
            }
        }
    }

    private void printTrainers() {
        try {
            System.out.println("Trainers:");
            System.out.println("_________");
            final List<Trainer> trainers = pokemonService.readTrainers();
            trainers.forEach(System.out::println);
            if (trainers.isEmpty()) System.out.println("No trainers, add a trainer first.");
        } catch (SQLException e) {
            logger.error("Error while reading trainers!", e);
        }
    }

    private void printWild() {
        try {
            System.out.println("Wild pokemon:");
            System.out.println("_____________");
            final List<Pokemon> pokemon = pokemonService.readWild();
            pokemon.forEach(System.out::println);
            if (pokemon.isEmpty()) System.out.println("There are no wild pokemon.");
        } catch (SQLException e) {
            logger.error("Error while reading wild pokemon!", e);
        }
    }

    private Trainer pickTrainer() throws SQLException {
        Trainer trainer;
        while (true) {
            System.out.println("0. Cancel");
            final List<Trainer> trainers = pokemonService.readTrainers();

            if (trainers.isEmpty()) {
                System.out.println("There are no trainers! Add a new trainer first.");
                return null;
            }

            for (int i = 0; i < trainers.size(); i++) {
                System.out.println((i + 1) + ". " + trainers.get(i));
            }

            int choiceTrainer = InputUtils.readInt();
            if (choiceTrainer == 0) {
                return null;
            } else if (choiceTrainer < 1 || choiceTrainer > trainers.size()) {
                System.out.println("Invalid choice");
            } else {
                trainer = trainers.get(choiceTrainer - 1);
                return trainer;
            }
        }
    }

    private void catchWild() {
        try {
            Pokemon pokemon;

            final List<Pokemon> pokemons = pokemonService.readWild();

            if (pokemons.isEmpty()) {
                System.out.println("There are no wild pokemon to catch!");
                return;
            }

            System.out.println("Which trainer is catching?");
            System.out.println("__________________________");
            Trainer trainer = pickTrainer();
            if (trainer == null) {
                System.out.println("Cancelled");
                return;
            }

            while (true) {
                System.out.println("Which pokemon is being caught by " + trainer + "?");
                System.out.println("________________________________________________________");
                System.out.println("0. Cancel");

                for (int i = 0; i < pokemons.size(); i++) {
                    System.out.println((i + 1) + ". " + pokemons.get(i));
                }

                int choicePokemon = InputUtils.readInt();

                if (choicePokemon == 0) {
                    return;
                } else if (choicePokemon < 1 || choicePokemon > pokemons.size()) {
                    System.out.println("Invalid choice");
                } else {
                    pokemon = pokemons.get(choicePokemon - 1);
                    break;
                }
            }
            if (pokemonService.catchPokemon(pokemon, trainer) > 0) {
                System.out.println(trainer + " caught the " + pokemon);
            } else {
                System.out.println("Pokemon could not be caught.");
            }
        } catch (SQLException e) {
            logger.error("Error while catching wild pokemon!", e);
        }
    }

    private void printTrainerPokemon() {
        try {
            System.out.println("Show pokemon of which trainer?");
            Trainer trainer = pickTrainer();
            if (trainer == null) {
                System.out.println("Cancelled");
                return;
            }

            pokemonService.readOwnedBy(trainer).forEach(System.out::println);
        } catch (SQLException e) {
            logger.error("Error while reading this trainer's pokemon!", e);
        }
    }

    private void printTrainerBest() {
        try {
            final List<Trainer> trainers = pokemonService.readBest();
            System.out.println("Trainers by pokemon:");
            System.out.println("____________________");
            for (int i = 0; i < trainers.size(); i++) {
                System.out.println((i + 1) + ": " + trainers.get(i).getName() + " with " + trainers.get(i).getPokemonOwned() + " pokemon owned.");
            }
        } catch (SQLException e) {
            logger.error("Error while reading trainers ordered by pokemon owned!", e);
        }
    }

    private void createTrainer() {
        try {
            System.out.println("Enter name:");
            final String name = InputUtils.readString();
            final int result = pokemonService.createTrainer(name);
            if (result > 0) {
                System.out.println(name + " successfully added");
            } else {
                System.out.println("Error, could not add trainer.");
            }
        } catch (SQLException e) {
            logger.error("Error while creating trainer!", e);
        }
    }

    private void createPokemonOwned() {
        try {
            int owner_id;
            System.out.println("Which trainer is getting a new pokemon?");
            Trainer trainer = pickTrainer();
            if (trainer == null) {
                System.out.println("Cancelled");
                return;
            }
            owner_id = trainer.getId();

            System.out.println("What is the pokemon's name?");
            final String name = InputUtils.readString();
            final int result = pokemonService.createOwned(name, owner_id);
            if (result > 0) {
                System.out.println(name + " is now " + trainer + "'s!");
            } else {
                System.out.println("Error, could not create pokemon.");
            }
        } catch (SQLException e) {
            logger.error("Error while creating pokemon!", e);
        }
    }

    private void createWild() {
        try {
            System.out.println("What is the pokemon's name?");
            final String name = InputUtils.readString();
            final int result = pokemonService.createWild(name);
            if (result > 0) {
                System.out.println("A wild " + name + " appears!");
            } else {
                System.out.println("Error, could not create pokemon.");
            }
        } catch (SQLException e) {
            logger.error("Error while creating wild pokemon!", e);
        }
    }
}

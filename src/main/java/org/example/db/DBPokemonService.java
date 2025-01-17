package org.example.db;

import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class DBPokemonService {
    private static final String READ_WILD = "SELECT * FROM pokemons WHERE owner_id IS NULL";
    private static final String CREATE_OWNED = "INSERT INTO pokemons (name, owner_id) VALUES (?, ?)";
    private static final String CREATE_WILD = "INSERT INTO pokemons (name) VALUES (?)";
    private static final String CATCH = "UPDATE pokemons SET owner_id = ? WHERE id = ?";
    private static final String READ_TRAINERS = "SELECT * FROM trainers";
    private static final String CREATE_TRAINERS = "INSERT INTO trainers (name) VALUES (?)";
    private static final String READ_OWNED_BY = "SELECT * FROM pokemons WHERE owner_id = ?";
    private static final String READ_BEST =
            "SELECT t.id, t.name, COUNT(p.name) AS pokemons_owned " +
                    "FROM trainers t " +
                    "JOIN pokemons p " +
                    "ON p.owner_id = t.id " +
                    "GROUP BY t.id,t.name " +
                    "ORDER BY pokemons_owned DESC";

    private static final Logger logger = getLogger(DBPokemonService.class);


    public List<Pokemon> readWild() {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_WILD)) {

            ResultSet resultSet = statement.executeQuery();
            List<Pokemon> pokemons = new ArrayList<>();
            while (resultSet.next()) {
                pokemons.add(new Pokemon(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
            return pokemons;
        } catch (SQLException e) {
            logger.error("Error while reading wild pokemon!", e);
            return null;
        }
    }

    public int createOwned(String name, int owner_id) {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_OWNED, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setInt(2, owner_id);

            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while creating pokemon!", e);
            return 0;
        }
    }

    public int createWild(String name) {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_WILD, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);

            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while creating wild pokemon!", e);
            return 0;
        }
    }

    public int catchPokemon(Pokemon pokemon, Trainer trainer) {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CATCH)) {

            statement.setInt(1, trainer.getId());
            statement.setInt(2, pokemon.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while editing pokemon!", e);
            return 0;
        }
    }

    public List<Trainer> readTrainers() {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_TRAINERS)) {

            ResultSet resultSet = statement.executeQuery();
            List<Trainer> trainers = new ArrayList<>();
            while (resultSet.next()) {
                trainers.add(new Trainer(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
            return trainers;
        } catch (SQLException e) {
            logger.error("Error while reading trainers!", e);
            return null;
        }
    }

    public int createTrainer(String name) {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_TRAINERS, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);

            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while creating wild pokemon!", e);
            return 0;
        }
    }

    public List<Pokemon> readOwnedBy(Trainer trainer) {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_OWNED_BY)) {

            statement.setInt(1, trainer.getId());

            ResultSet resultSet = statement.executeQuery();
            List<Pokemon> pokemons = new ArrayList<>();
            while (resultSet.next()) {
                pokemons.add(new Pokemon(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
            return pokemons;
        } catch (SQLException e) {
            logger.error("Error while reading owned pokemon!", e);
            return null;
        }
    }

    public List<Trainer> readBest() {
        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_BEST)) {

            ResultSet resultSet = statement.executeQuery();
            List<Trainer> trainers = new ArrayList<>();
            while (resultSet.next()) {
                trainers.add(new Trainer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("pokemons_owned")
                ));
            }
            return trainers;
        } catch (SQLException e) {
            logger.error("Error while reading trainers!", e);
            return null;
        }
    }

}

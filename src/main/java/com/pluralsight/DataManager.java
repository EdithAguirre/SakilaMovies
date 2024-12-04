package com.pluralsight;

import javax.sql.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private DataSource dataSource;

    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Method to search for actors by name and return a list of actors
    public List<Actor> getAllActors(String lastName) {
        List<Actor> actors = new ArrayList<>();

        String query = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, lastName);

            try (
                    ResultSet results = statement.executeQuery()
            ) {
                if (results.next()) {
                    do {
                        // process results
                        actors.add(new Actor(results.getInt(1), results.getString(2),
                                results.getString(3)));
                    } while (results.next());
                } else {
                    System.out.println("No matches!");
                }

            } catch (SQLException e) {
                System.out.println("Failed to execute query.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect sql or to create prepared statement.");
        }
        return actors;
    }

    // Method to return a list of films by actor id
    public List<Film> getAllFilms(int filmId) {
        List<Film> films = new ArrayList<>();

        String query = "SELECT f.film_id, f.title, f.description, f.release_year, f.length FROM film_actor AS fa \n" +
                "JOIN film AS f ON (fa.film_id = f.film_id) JOIN actor AS a ON (fa.actor_id = a.actor_id) \n" +
                "WHERE a.actor_id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, filmId);

            try (
                    ResultSet results = statement.executeQuery()
            ) {
                if (results.next()) {
                    do {
                        // process results
                        films.add(new Film(results.getInt(1), results.getString(2),
                                results.getString(3), results.getInt(4), results.getInt(5)));
                    } while (results.next());
                } else {
                    System.out.println("No matches!");
                }

            } catch (SQLException e) {
                System.out.println("Failed to execute query.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect sql or to create prepared statement.");
        }
        return films;
    }

}

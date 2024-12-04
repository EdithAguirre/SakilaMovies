package com.pluralsight;
import java.sql.*;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;
public class Main {
    public static void main(String[] args){
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.hca.jdbc.UsingDriverManager <username> " +
                            "<password>");
            System.exit(1);
        }

        // get the username and password from the command line args
        String url = "jdbc:mysql://localhost:3306/sakila";
        String username = args[0];
        String password = args[1];

        // Create and configure the DataSource
        try (
                BasicDataSource dataSource = new BasicDataSource()
        ) {
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        } catch (SQLException e) {
            System.out.println("Fail in creating data source.");
        }

        // Create the connection and prepared statement in a try-with-resources block
        try (
                Connection connection = DriverManager.getConnection(url, username, password)
        ) {
            // get the actors with the same last name as the one the user inputs
            getSameLastNameActors(connection);

            // get list of films that an actor the user entered has been in
            getListOfFilms(connection);

        } catch (SQLException e) {
            System.out.println("Connection failed.");
        }
    }

    private static void getListOfFilms(Connection connection) {
        // Prompt user for fist name and last name of an actor they want to see the movies of
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the full name of an actor you'd like to see the movies of.");
        System.out.print("Enter the first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter the last name: ");
        String lastName = scanner.nextLine().trim();

        // Use first and last name to display a list of the films that actor has been in.
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT f.film_id, f.title, f.description, f.release_year FROM film_actor AS fa \n" +
                                "JOIN film AS f ON (fa.film_id = f.film_id) JOIN actor AS a ON (fa.actor_id = a.actor_id) \n" +
                                "WHERE a.actor_id = (SELECT a.actor_id FROM actor WHERE a.first_name = ? \n" +
                                "AND a.last_name = ? LIMIT 1)")
        ) {
            // Set parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            // Execute the query
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                // use the first call to next() to see if there are records
                if (resultSet.next()) {
                    System.out.println("Your matches are: \n");
                    System.out.printf("%-8s %-23s %-120s %s", "Film_id", "Title", "Description", "Release_Year\n");
                    System.out.printf("%-8s %-23s %-120s %s", "--------","-----------------------",
                            "------------------------------------------------------------------------------------------------------------------------"
                            , "-------------------\n");

                    // if there are, you are already sitting on the first one so
                    // switch your loop to using a do/while
                    do {
                        // process results
                        System.out.printf("%-8s %-23s %-120s %s\n", resultSet.getString(1), resultSet.getString(2),
                                resultSet.getString(3), resultSet.getString(4));
                    } while (resultSet.next());
                } else {
                    System.out.println("No matches!");
                }
                System.out.println();

            } catch (SQLException e) {
                System.out.println("Failed to execute query.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Failed to create a prepared statement.");
        }
    }

    private static void getSameLastNameActors(Connection connection) {
        // Prompt user for a last name of an actor they like
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the last name of an actor you like: ");
        String lastName = scanner.nextLine().trim();

        // Use last name to display a list of all actors with that last name
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT first_name, last_name FROM actor WHERE last_name = ?")
        ) {
            // Set parameters
            preparedStatement.setString(1, lastName);

            // Execute the query
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                // use the first call to next() to see if there are records
                if (resultSet.next()) {
                    System.out.println("Your matches are: \n");
                    System.out.printf("%-20s %s", "First_Name", "Last_Name\n");
                    System.out.printf("%-20s %s", "-------------------", "-------------------\n");
                    // if there are, you are already sitting on the first one so
                    // switch your loop to using a do/while
                    do {
                        // process results
                        System.out.printf("%-20s %s\n", resultSet.getString(1), resultSet.getString(2));
                    } while (resultSet.next());
                } else {
                    System.out.println("No matches!");
                }
                System.out.println();

            } catch (SQLException e) {
                System.out.println("Failed to execute query.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Failed to create a prepared statement.");
        }

    }
}

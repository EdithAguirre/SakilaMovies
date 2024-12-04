package com.pluralsight;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Main {
    public static void main(String[] args) {
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
            // Configure the dataSource
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            // Create the dataManager
            DataManager dataManager = new DataManager(dataSource);

            Scanner scanner = new Scanner(System.in);
            // Prompt the user to search for actor by name and display the results of the search
            System.out.print("Enter the last name of an actor you like: ");
            String lastName = scanner.nextLine().trim();
            List<Actor> actors = dataManager.getAllActors(lastName);

            // display actors
            System.out.println("\nYour matches are: \n");
            System.out.printf("%-10s %-15s %s", "Actor_Id", "First_Name", "Last_Name\n");
            System.out.printf("%-10s %-15s %s", "----------", "---------------", "---------------\n");
            actors.forEach(System.out::println);
            System.out.println();

            // Prompt the user to enter an actor id, then search for and display a list of movies
            // that the actor has appeared in
            System.out.print("Enter an actor id to search for and display a list of movies the actor appeared in: ");
            int actorId = scanner.nextInt();
            scanner.nextLine();
            List<Film> films = dataManager.getAllFilms(actorId);

            // display films
            System.out.println("\nYour matches are: \n");
            System.out.printf("%-8s %-23s %-120s %-13s %s", "Film_id", "Title", "Description", "Release_Year", "Length\n");
            System.out.printf("%-8s %-23s %-120s %-13s %s", "--------", "-----------------------",
                    "------------------------------------------------------------------------------------------------------------------------"
                    , "-------------", "-------\n");
            films.forEach(System.out::println);

        } catch (SQLException e) {
            System.out.println("Fail in creating data source.");
        }

    }
}

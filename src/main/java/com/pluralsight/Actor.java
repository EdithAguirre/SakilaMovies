package com.pluralsight;

public class Actor {
    private int actorId;
    private String firstName, lastName;


    public Actor(int actorId, String firstName, String lastName) {
        this.actorId = actorId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-15s %s", this.actorId, this.firstName, this.lastName);
    }
}

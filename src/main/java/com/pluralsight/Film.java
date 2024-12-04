package com.pluralsight;

public class Film {
    private int filmId, releaseYear, length;
    private String title, description;

    public Film(int filmId, String title, String description, int releaseYear, int length) {
        this.filmId = filmId;
        this.releaseYear = releaseYear;
        this.length = length;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%-8d %-23s %-120s %-13d %d", this.filmId, this.title,
                this.description, this.releaseYear, this.length);
    }
}

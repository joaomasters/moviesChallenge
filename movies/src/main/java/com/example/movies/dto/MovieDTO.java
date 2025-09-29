package com.example.movies.dto;


public class MovieDTO {
    private int year;
    private String title;
    private String studios;
    private boolean winner;

    public MovieDTO(int year, String title, String studios, boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.winner = winner;
    }

    public int getYear() { return year; }
    public String getTitle() { return title; }
    public String getStudios() { return studios; }
    public boolean isWinner() { return winner; }
}

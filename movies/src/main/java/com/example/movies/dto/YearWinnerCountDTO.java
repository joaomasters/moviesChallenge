package com.example.movies.dto;

public class YearWinnerCountDTO {
    private final int year;
    private final long winnerCount;

    public YearWinnerCountDTO(int year, long winnerCount) {
        this.year = year;
        this.winnerCount = winnerCount;
    }

    public int getYear() {
        return year;
    }

    public long getWinnerCount() {
        return winnerCount;
    }
}

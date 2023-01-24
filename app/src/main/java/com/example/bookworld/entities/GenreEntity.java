package com.example.bookworld.entities;

import com.example.bookworld.beans.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreEntity {

    private List<Genre> results = new ArrayList<Genre>();

    public List<Genre> getResults() {
        return results;
    }

    public void setResults(List<Genre> results) {
        this.results = results;
    }
}

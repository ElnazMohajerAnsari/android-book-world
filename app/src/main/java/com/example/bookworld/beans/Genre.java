package com.example.bookworld.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Genre implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String genreId;
    private String name;
    private String image;

    @Ignore
    public Genre() {
    }

    public Genre(@NonNull String genreId, String name, String image) {
        this.genreId = genreId;
        this.name = name;
        this.image = image;
    }

    public Genre(@NonNull String genreId, String image, String name, String createdAt, String updatedAt) {
        this.genreId = genreId;
        this.name = name;
        this.image = image;
    }

    @NonNull
    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(@NonNull String genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id='" + genreId + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

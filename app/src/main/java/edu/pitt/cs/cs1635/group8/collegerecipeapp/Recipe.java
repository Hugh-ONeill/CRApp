package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    public String name;
    public double price;
    public double rating;
    @ColumnInfo(name = "rating_count")
    public int ratingCount;
    @ColumnInfo(name = "picture_id")
    public String pictureId;

    public String ingredients;
    public String directions;

    @Ignore
    public double portions = 1;

    public Recipe() {
        
    }

    public Recipe(String name) {
        this.name = name;
        this.price = 0;
        this.rating = 0;
        this.ratingCount = 0;
        this.pictureId = null;
        this.ingredients = "";
        this.directions = "";
    }

    @Ignore
    public Recipe(String name, double price, String ingredients) {
        this.name = name;
        this.price = price;
        this.rating = 0;
        this.ratingCount = 0;
        this.pictureId = null;
        this.ingredients = ingredients;
        this.directions = "";
    }

    public void updateRating(double newRating) {
        this.ratingCount++;
        this.rating = this.rating + (newRating - this.rating) / (this.ratingCount);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    @Ignore
    public double getPortions() {
        return portions;
    }

    @Ignore
    public void setPortions(double portions) {
        this.portions = portions;
    }
}
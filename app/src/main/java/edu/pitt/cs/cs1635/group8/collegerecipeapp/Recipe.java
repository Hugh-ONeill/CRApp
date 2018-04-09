package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private final Integer id = null;

    private String name;
    private float price;
    private float rating;
    @ColumnInfo(name = "rating_count")
    private float ratingCount;
    @ColumnInfo(name = "picture_id")
    private int pictureId;

    private String ingredients;
    private String directions;

    public Recipe(String name) {
        this.name = name;
        this.price = 0;
        this.rating = 0;
        this.ratingCount = 0;
        this.pictureId = 0;
        this.ingredients = "";
        this.directions = "";
    }

    public void updateRating(float newRating) {
        this.ratingCount++;
        float updatedAverageRating = this.rating + (newRating - this.rating) / (this.ratingCount);
        this.rating = updatedAverageRating;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
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
}
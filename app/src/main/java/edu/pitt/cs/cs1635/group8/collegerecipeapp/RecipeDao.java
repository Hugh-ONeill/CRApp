package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe... recipes);

    @Update()
    void update(Recipe... recipes);

    @Delete()
    void delete(Recipe... recipes);

    @Query("SELECT * FROM recipe ORDER BY rating ASC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe ORDER BY name ASC")
    LiveData<List<Recipe>> getAllRecipesByName();

    @Query("SELECT * FROM recipe WHERE name LIKE '%'+:keyWord+'%' ORDER BY rating ASC")
    LiveData<List<Recipe>> getRecipesByKeyword(String keyWord);

    @Query("SELECT * FROM recipe WHERE ingredients LIKE '%'+:tag+'%' ORDER BY rating ASC")
    LiveData<List<Recipe>> getRecipesByTag(String tag);

    @Query("SELECT * FROM recipe WHERE ingredients LIKE :tags ORDER BY rating ASC")
    LiveData<List<Recipe>> getRecipesByTags(String tags);
}

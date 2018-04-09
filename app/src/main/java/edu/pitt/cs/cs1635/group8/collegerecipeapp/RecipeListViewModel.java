package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private LiveData<List<Recipe>> allRecipes;

    public RecipeListViewModel (Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        allRecipes = recipeRepository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public void insertRecipe(Recipe recipe) {
        recipeRepository.insertRecipe(recipe);
    }
}

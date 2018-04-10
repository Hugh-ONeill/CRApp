package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private LiveData<List<Recipe>> recipeList;

    public RecipeListViewModel (Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        //if (recipeList == null) {
        //    recipeList = new MutableLiveData<>();
        //    loadRecipes();
        //}
        //return recipeList;
        return recipeRepository.getAllRecipes();
    }

    public LiveData<Recipe> getRecipe(int recipeId) {
        return recipeRepository.getRecipe(recipeId);
    }

    public void insertRecipe(Recipe recipe) {
        recipeRepository.insertRecipe(recipe);
    }

    public void clearTable() { recipeRepository.clearTable(); }
}

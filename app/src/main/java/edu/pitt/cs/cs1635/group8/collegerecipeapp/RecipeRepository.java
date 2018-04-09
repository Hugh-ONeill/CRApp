package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class RecipeRepository {

    private RecipeDao recipeDao;
    private List<Recipe> allRecipes;

    RecipeRepository(Application application) {
        RecipeDatabase db = RecipeDatabase.getInstance(application);
        recipeDao = db.getRecipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    public List<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public void insertRecipe (Recipe recipe) { new insertAsyncTask(recipeDao).execute(recipe); }

    private static class insertAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeDao asyncTaskDao;

        insertAsyncTask(RecipeDao thisDao) {
            asyncTaskDao = thisDao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

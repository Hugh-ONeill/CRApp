package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class RecipeRepository {

    private RecipeDao recipeDao;

    RecipeRepository(Application application) {
        RecipeDatabase db = RecipeDatabase.getInstance(application);
        recipeDao = db.getRecipeDao();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return recipeDao.getAllRecipes();
    }

    public LiveData<Recipe> getRecipe(int recipeId) { return recipeDao.getRecipe(recipeId); }

    public void insertRecipe (Recipe recipe) { new insertAsyncTask(recipeDao).execute(recipe); }

    public void clearTable() { new clearAsyncTask(recipeDao).execute(); }

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

    private static class clearAsyncTask extends AsyncTask<Void, Void, Void> {

        private RecipeDao asyncTaskDao;

        clearAsyncTask(RecipeDao thisDao) {
            asyncTaskDao = thisDao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            asyncTaskDao.clearTable();
            return null;
        }
    }
}

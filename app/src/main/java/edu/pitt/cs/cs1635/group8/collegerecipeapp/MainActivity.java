package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView searchRecycler;
    private RecipeListViewModel recipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchRecycler = findViewById(R.id.search_recycler);
        final RecipeAdapter recipeAdapter = new RecipeAdapter(this);
        searchRecycler.setAdapter(recipeAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setItemAnimator(new DefaultItemAnimator());

        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        //recipeListViewModel.insertRecipe(new Recipe("Pizza", 9.0, "Cheese, Sauce"));
        //recipeListViewModel.insertRecipe(new Recipe("Cake", 6.0, "Flour, Sugar, Egg"));
        //recipeListViewModel.insertRecipe(new Recipe("Hotdog", 1.0, "Hotdog, Bun"));
        //recipeListViewModel.insertRecipe(new Recipe("Hotdogger", 1.0, "Actual Dog, Bun"));
        //recipeListViewModel.insertRecipe(new Recipe("Fairy Drink", 1.0, "Sugar, Tears"));

        recipeListViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipeList) {
                recipeAdapter.setRecipeList(recipeList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
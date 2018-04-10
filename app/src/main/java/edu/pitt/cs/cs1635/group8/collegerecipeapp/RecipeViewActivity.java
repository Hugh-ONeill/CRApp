package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewActivity extends AppCompatActivity {

    private SectionsPagerAdapter recipeSectionsPagerAdapter;
    private ViewPager recipeViewPager;
    private RecipeListViewModel recipeListViewModel;
    private Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipeId", 0);
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        recipeListViewModel.getRecipe(recipeId).observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable final Recipe thisRecipe) {
                currentRecipe = thisRecipe;
            }
        });

        setContentView(R.layout.activity_recipe_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipeSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        recipeViewPager = findViewById(R.id.recipe_container);
        recipeViewPager.setAdapter(recipeSectionsPagerAdapter);

        TabLayout recipeTabs = findViewById(R.id.recipe_tabs);
        recipeTabs.setupWithViewPager(recipeViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Recipe getCurrentRecipe()
    {
        return currentRecipe;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 3;
        private String[] sectionHeaders =  new String[] {"Main", "Ingredients", "Directions"};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    RecipeViewMainFragment recipeViewMainFragment = new RecipeViewMainFragment();
                    return recipeViewMainFragment;
                case 1:
                    RecipeViewIngredientsFragment recipeViewIngredientsFragment = new RecipeViewIngredientsFragment();
                    return recipeViewIngredientsFragment;
                case 2:
                    RecipeViewDirectionsFragment recipeViewDirectionsFragment = new RecipeViewDirectionsFragment();
                    return recipeViewDirectionsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sectionHeaders[position];
        }
    }

    public static class RecipeViewMainFragment extends Fragment {

        public RecipeViewMainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            RecipeViewActivity thisActivity = (RecipeViewActivity) getActivity();
            Recipe currentRecipe = thisActivity.getCurrentRecipe();

            View rootView = inflater.inflate(R.layout.fragment_recipe_view_main, container, false);
            TextView nameText = rootView.findViewById(R.id.recipe_name_textbox);
            Spinner portionSpinner = rootView.findViewById(R.id.portion_spinner);
            TextView priceText = rootView.findViewById(R.id.price_value_textbox);

            nameText.setText(currentRecipe.getName());
            portionSpinner.setSelection(3);
            priceText.setText(String.valueOf(currentRecipe.getPrice()));
            return rootView;
        }
    }

    public static class RecipeViewIngredientsFragment extends Fragment {

        public RecipeViewIngredientsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recipe_view_ingredients, container, false);
            RecyclerView ingredientList = rootView.findViewById(R.id.ingredient_list);

            RecipeViewActivity thisActivity = (RecipeViewActivity) getActivity();
            Recipe currentRecipe = thisActivity.getCurrentRecipe();
            List<String> ingredients = new ArrayList<>();
            ingredients.add("Cheese");
            ingredients.add("Sauce");

            final IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext());
            ingredientList.setAdapter(ingredientAdapter);
            ingredientList.setLayoutManager(new LinearLayoutManager(getContext()));
            ingredientList.setItemAnimator(new DefaultItemAnimator());

            ingredientAdapter.setIngredientList(ingredients);

            return rootView;
        }
    }

    public static class RecipeViewDirectionsFragment extends Fragment {

        public RecipeViewDirectionsFragment () {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recipe_view_directions, container, false);
            RecyclerView directionList = rootView.findViewById(R.id.direction_list);

            RecipeViewActivity thisActivity = (RecipeViewActivity) getActivity();
            Recipe currentRecipe = thisActivity.getCurrentRecipe();
            List<String> directions = new ArrayList<>();
            directions.add("Do Thing");
            directions.add("Do Thing Again");

            final DirectionAdapter directionAdapter = new DirectionAdapter(getContext());
            directionList.setAdapter(directionAdapter);
            directionList.setLayoutManager(new LinearLayoutManager(getContext()));
            directionList.setItemAnimator(new DefaultItemAnimator());

            directionAdapter.setDirectionList(directions);

            return rootView;
        }
    }
}
package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
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

public class RecipeViewActivity extends AppCompatActivity {

    private SectionsPagerAdapter recipeSectionsPagerAdapter;
    private ViewPager recipeViewPager;
    private String recipeName;
    private int recipeCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipeSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        recipeViewPager = findViewById(R.id.recipe_container);
        recipeViewPager.setAdapter(recipeSectionsPagerAdapter);

        TabLayout recipeTabs = findViewById(R.id.recipe_tabs);
        recipeTabs.setupWithViewPager(recipeViewPager);

        Intent thisIntent = getIntent();
        String[] recipeData = thisIntent.getStringExtra("recipeInfo").split(":");
        System.out.println(recipeData);

        recipeName = recipeData[0];
        recipeCost = Integer.valueOf(recipeData[1]);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 3;
        private String[] sectionHeaders =  new String[] {recipeName, "Ingredients", "Directions"};

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
            View rootView = inflater.inflate(R.layout.fragment_recipe_view_main, container, false);
            Spinner portionSpinner = rootView.findViewById(R.id.portion_spinner);
            portionSpinner.setSelection(3);
            return rootView;
        }
    }

    public static class RecipeViewIngredientsFragment extends Fragment {

        public RecipeViewIngredientsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recipe_view_ingredients, container, false);
            return rootView;
        }
    }

    public static class RecipeViewDirectionsFragment extends Fragment {

        public RecipeViewDirectionsFragment () {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recipe_view_directions, container, false);
            return rootView;
        }
    }
}
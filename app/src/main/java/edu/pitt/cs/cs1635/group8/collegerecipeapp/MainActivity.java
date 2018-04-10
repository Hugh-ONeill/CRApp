package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    private int TAGS = 0;
    private ArrayList<Button> tags;

    private RecyclerView searchRecycler;
    private RecipeListViewModel recipeListViewModel;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private List<Recipe> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tags = new ArrayList();

        SearchView recipeSearch = findViewById(R.id.recipe_search);
        recipeSearch.setOnQueryTextListener(this);

        searchRecycler = findViewById(R.id.search_recycler);
        recipeAdapter = new RecipeAdapter(this);
        searchRecycler.setAdapter(recipeAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setItemAnimator(new DefaultItemAnimator());

        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        //recipeListViewModel.clearTable();

        //recipeListViewModel.insertRecipe(new Recipe("Pizza", 9.0, "Cheese,1;Sauce,1"));
        //recipeListViewModel.insertRecipe(new Recipe("Cake", 6.0, "Flour,2;Sugar,1;Egg,2"));
        //recipeListViewModel.insertRecipe(new Recipe("Hotdog", 1.0, "Hotdog,1;Bun,1"));
        //recipeListViewModel.insertRecipe(new Recipe("Hotdogger", 1.0, "Actual Dog,1;Bun,2"));
        //recipeListViewModel.insertRecipe(new Recipe("Fairy Drink", 1.0, "Sugar,3;Tears,5"));

        recipeListViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                recipeList = recipes;
            }
        });

        FloatingActionButton addRecipeButton = findViewById(R.id.add_recipe_button);
        addRecipeButton.setOnClickListener(onClickAddRecipeButton());
        Spinner searchBySpinner = findViewById(R.id.search_by_spinner);
        Spinner tagOptionSpinner = findViewById(R.id.contains_or_not_spinner);

        searchBySpinner.setOnItemSelectedListener(this);
        tagOptionSpinner.setOnItemSelectedListener(this);
    }

    private View.OnClickListener onClickAddRecipeButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        };
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

    @Override
    public boolean onQueryTextSubmit(String s) { //when the user submits a search query it adds a tag
        TAGS++; //increment the global tag counter for tag id maintenance
        RelativeLayout parentLayout = findViewById(R.id.tag_container);  //get the tag container, a relative layout between searchbar and scrollview
        parentLayout.addView(createNewButton(s)); //creates a new tag, tags are buttons to facilitate removal
        onQueryTextChange(s); //update the recipes being shown to user
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<Recipe> thisSearchList = new ArrayList<Recipe>();
        ArrayList<Recipe> thisRecipeList = (ArrayList) recipeList;

        Spinner searchby = findViewById(R.id.search_by_spinner);

        if(thisRecipeList.size() == 0){
            //showMessage("Error", "No Data"); //Error handling for empty database
            return false;
        }
        int j = 0;
        while(j < thisRecipeList.size()) { //move through all the recipes
            StringBuffer build = new StringBuffer();  //string builder for recipe display data
            build.append(thisRecipeList.get(j).getName() + "\n"); //add the name to builder
            build.append(":$" + thisRecipeList.get(j).getPrice() + "\n"); //add the price to builder, semicolon for easy splitting
            if(searchby.getSelectedItem().equals("By Keyword")){  //if user is searching by keyword
                if(build.toString().toUpperCase().contains(s.toUpperCase())){  //this ignores cases and checks if user keyword is present in name
                    thisSearchList.add(thisRecipeList.get(j));
                }
            }
            else {  //If the user is searching by ingredient
                if(tags.size() != 0) { //if the user is using tags
                    int matchesCriteria = 0; //incrementer to make sure recipe matches all user criteria specified within tags
                    for (int i = 0; i < tags.size(); i++) { //for all of the tags
                        String theIngredient = tags.get(i).getText().toString().toUpperCase().substring(1); //remove the + or - icon, which is added for user awareness of tag use
                        String withOrWithout = tags.get(i).getText().toString().substring(0,1); //get the ingredient name
                        if(withOrWithout.equals("+")){ //if the tag was an ingredient the user wanted
                            if(thisRecipeList.get(j).getIngredients().toUpperCase().contains((tags.get(i).getText().toString().toUpperCase().substring(1)))) { //check if the ingredient is in the recipe
                                matchesCriteria++; //if it does, increment the amount of criteria it matches
                            }
                        }
                        else{ //this is an ingredient the user wants to avoid
                            if(!thisRecipeList.get(j).getIngredients().toUpperCase().contains(theIngredient)){ //make sure it's not in the recipe
                                matchesCriteria++; //if so, increment the amount of criteria it matches
                            }
                        }
                    }
                    if (matchesCriteria == tags.size()) {  //if the recipe matches all the users criteria
                        thisSearchList.add(thisRecipeList.get(j));
                    }
                }
                else{  //if the user is not using tags
                    Spinner withOrWithout = findViewById(R.id.contains_or_not_spinner);  //get the with or without spinner
                    if(withOrWithout.getSelectedItem().toString().equals("With")) { //check if user is searching for recipes with or without ingredients, this is with
                        if (thisRecipeList.get(j).getIngredients().toUpperCase().contains(s.toUpperCase())) { //ignore case and check if recipe contains ingredient
                            thisSearchList.add(thisRecipeList.get(j));
                        }
                    }
                    else{  //this is searching without recipe
                        if(!thisRecipeList.get(j).getIngredients().toUpperCase().contains(s.toUpperCase())){  //make sure the recipe does not include ingredient
                            thisSearchList.add(thisRecipeList.get(j));
                        }
                    }
                }
            }
            j++;
        }
        recipeAdapter.setRecipeList(thisSearchList);
        return true;
    }

    private Button createNewButton(String text){
        final RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lparams.addRule(RelativeLayout.BELOW, (R.id.recipe_search)); //make sure all tags are below
        if(TAGS != 1){ //this parameter is unnecessary for the first tag
            lparams.addRule(RelativeLayout.RIGHT_OF, TAGS-1);
        }
        final Button newTag = new Button(this); //new button to be added
        newTag.setLayoutParams(lparams); //set the parameters that wrap content for width and height and set it to right of rightmost tag

        //Logic for checking whether user wants recipes with or without ingredients
        Spinner withOrWithout = findViewById(R.id.contains_or_not_spinner); //get with or without option
        if(withOrWithout.getSelectedItem().toString().equals("With")){ //if with, set the "with ingredient" button style
            newTag.setText("+" + text);
            newTag.setBackgroundResource(R.drawable.button_selector);
        }
        else{ //if without, set the "without ingredient" button style
            newTag.setText("-" + text);
            newTag.setBackgroundResource(R.drawable.button_selector_remove);
        }

        newTag.setId(TAGS); //sets id of the button for handling

        newTag.setOnClickListener(new View.OnClickListener() { //removes the button from screen and internal list of buttons
            @Override
            public void onClick(View view) {
                RelativeLayout parentLayout = findViewById(R.id.tag_container); //access the tag container view
                View tagToRemove = parentLayout.findViewById(view.getId()); //get the button to be removed
                if(tags.indexOf(tagToRemove) > 0  && tags.indexOf(tagToRemove) < tags.size()-1) { //if the tag being removed is not the beginning or end of the list of tags
                    View tagToSwapAlignment = parentLayout.findViewById(tags.get(tags.indexOf(tagToRemove)+1).getId()); //get the tag to the right of this one
                    tagToSwapAlignment.setLayoutParams(tagToRemove.getLayoutParams()); //swap their layout parameters to maintain tag format
                }
                parentLayout.removeView(tagToRemove); //finally, remove it from the view
                tags.remove(tagToRemove); //then remove it from the internal list

                SearchView searchString = findViewById(R.id.recipe_search);
                onQueryTextChange(searchString.getQuery().toString()); //recall the query method to update the displayed recipes
            }
        });
        tags.add(newTag); //add to the list of tags
        return newTag; //send it back for adding to review
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.getId() == R.id.contains_or_not_spinner){
            withOrWithout(adapterView, view, i, l);
        }
        else{
            searchBySelected(adapterView, view, i, l);
        }
    }

    private void searchBySelected(AdapterView<?> adapterView, View view, int i, long l){
        Spinner containsOrNot = findViewById(R.id.contains_or_not_spinner);
        if(adapterView.getSelectedItem().toString().equals("By Keyword")) {
            containsOrNot.setVisibility(View.INVISIBLE);
        }
        else{
            containsOrNot.setVisibility(View.VISIBLE);
        }
        SearchView searchBar = findViewById(R.id.recipe_search);
        onQueryTextChange(searchBar.getQuery().toString());
    }

    private void withOrWithout(AdapterView<?> adapterView, View view, int i, long l){
        SearchView getSearchedIngredient = findViewById(R.id.recipe_search);
        String theIngredient = getSearchedIngredient.getQuery().toString();
        onQueryTextChange(theIngredient);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
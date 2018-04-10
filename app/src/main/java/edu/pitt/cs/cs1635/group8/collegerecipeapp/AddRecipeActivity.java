package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Globals, some of which might be able to be reduced to locals, feel free to try it out
    private static final int SELECTED_PICTURE=1;
    ImageView recipePic;
    Spinner spinner;
    ArrayList<Spinner> spinners = new ArrayList<>();
    Button addIngredient, save;
    LinearLayout mLayout;
    ArrayList<EditText> ingredient = new ArrayList<>();
    StringBuilder ingredientList = new StringBuilder();
    EditText mEditText, recipeName, price, directions;
    ArrayAdapter<CharSequence> adapter;
    int numIngredients = 0;
    String usersPicture = null;
    private FirebaseAuth mAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("recipes");

    RecipeListViewModel recipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        mAuth = FirebaseAuth.getInstance();
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        spinner = findViewById(R.id.spinner2);  //initial spinner for amounts
        //adapter = ArrayAdapter.createFromResource(this, R.array.portion_modifiers, android.R.layout.simple_spinner_item); //create adapter with amounts resource
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //set the labels with the amounts resource
        //spinner.setAdapter(adapter);    //add the adapter to the amount spinner
        spinner.setOnItemSelectedListener(this);    //set the listener for the spinner

        addIngredient = findViewById(R.id.button2); //get the ingredient textbox
        addIngredient.setOnClickListener(onClick());    //set its listener

        save = findViewById(R.id.save); //save button
        save.setOnClickListener(onSaveClick()); //save listener

        mLayout = findViewById(R.id.linear);    //get the layout within the scrollview
        mEditText = findViewById(R.id.editText4);   //Get the ingredient box NOTE:whoever makes create recipe prettier could change the view IDs to reflect their purpose
        EditText newIngredient = new EditText(this);    //I don't think this does anything but I didn't want to delete, possibly break the code and send it, check if its useless
        newIngredient.setHint("@string/ing_nam");   //set the possibly useless edit texts hint

        Button addImg = findViewById(R.id.picture); //add the picture button
        addImg.setOnClickListener(onImgClick());    //add its listener

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){   //check if we have permission to access photos
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);   //this requests the users permission to access photos
        }

    }

    private View.OnClickListener onSaveClick() {    //save listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner1 = findViewById(R.id.spinner2); //get the first ingredients spinner
                EditText ingredient1 = findViewById(R.id.editText4);    //get the first ingredients textbox
                ingredientList.append(ingredient1.getText().toString());    //add the ingedient to the list
                ingredientList.append("," + spinner1.getSelectedItem().toString() + ";");   //add its amount to the list
                for (int i = 0; i < numIngredients; i++) {  //for the rest of the dynamically created ingredients, add them and amounts to the list
                    ingredientList.append(ingredient.get(i).getText().toString());
                    ingredientList.append("," + spinners.get(i).getSelectedItem().toString() + ";");
                }

                EditText name = findViewById(R.id.editText5);   //get the text box for the recipe name
                String recipeName = name.getText().toString();  //save the name
                EditText price = findViewById(R.id.price);  //get the price box
                double recipePrice = 0.0;
                if (!price.getText().toString().isEmpty() && !(price.getText().toString() == null)) {
                    recipePrice = Double.parseDouble(price.getText().toString()); //save the price
                }
                EditText directions = findViewById(R.id.editText3); //get the directions box
                String recipeDirections = directions.getText().toString();  //save the directions

                Recipe thisRecipe = new Recipe(recipeName);
                thisRecipe.setPrice(recipePrice);
                thisRecipe.setIngredients(ingredientList.toString());
                thisRecipe.setDirections(recipeDirections);
                thisRecipe.setPictureId(usersPicture);

                String key = myRef.push().getKey();
                myRef.child(key).setValue(thisRecipe);

                recipeListViewModel.insertRecipe(thisRecipe);

                //if(isInserted){ //this updates the user with a toast about whether their recipe was created or not
                //    Toast.makeText(MainActivity.this,"Recipe Created", Toast.LENGTH_LONG).show();
                //}
                //else{
                //    Toast.makeText(MainActivity.this,"Recipe not Created", Toast.LENGTH_SHORT).show();
                //}
                Intent intent = new Intent(AddRecipeActivity.this, MainActivity.class);  //set intent to move the user to the search activity
                startActivity(intent);  //call the search activity
            }
        };
    }

    private View.OnClickListener onImgClick(){  //add picture listener
        return new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);    //set the intent for the photo gallery activity
                startActivityForResult(i, SELECTED_PICTURE);    //sends the user to their photo gallery
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){  //the users gallery image selection returns to this methods activation
        super.onActivityResult(requestCode, resultCode, data);  //override stuff
        switch(requestCode){    //gets the request type
            case SELECTED_PICTURE:  //if the user did select a picture
                if(resultCode==RESULT_OK){  //if the result says the selection worked
                    Uri uri=data.getData(); //get the image data
                    String[] projection = {MediaStore.Images.Media.DATA};   //more image data stuff I think
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);  //cursor for the image
                    cursor.moveToFirst();   //move to their image selection

                    int columnIndex = cursor.getColumnIndex(projection[0]); //more picture retrieval stuff, seriously, don't ask me
                    usersPicture = cursor.getString(columnIndex);   //and we've got the file path of the picture, all we need
                    cursor.close(); //close the cursor
                }
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {  //item selection for amount of ingredient spinner
        String choice = adapterView.getItemAtPosition(i).toString();    //get the amount
        Toast.makeText(adapterView.getContext(), choice, Toast.LENGTH_SHORT).show();    //fun little toast shows the users chosen amount
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { //they didn't choose anything, then they get nothing

    }

    private View.OnClickListener onClick() {    //add another ingredient spinner and textbox
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayout.addView(createNewTextView(mEditText.getText().toString())); //the call for adding a text box
                mLayout.addView(createNewSpinner());    //the call for adding an amount spinner
            }
        };
    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);    //params for text view
        final EditText newIngredient = new EditText(this);  //new text box
        ingredient.add(newIngredient);  //add the text box to the list of ingredient text boxes
        newIngredient.setLayoutParams(lparams); //set its parameters to those shown above
        newIngredient.setHint("Ingredient goes here");  //set a hint for the box
        newIngredient.setId(numIngredients++);  //set its id for access
        return newIngredient;   //give it to the caller
    }

    private Spinner createNewSpinner(){
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);    //spinner params
        final Spinner newAmount = new Spinner(this);    //new spinner for amount
        spinners.add(newAmount);    //add it to the list of amount spinners
        newAmount.setLayoutParams(lparams); //add the parameters to the new spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.portion_modifiers, android.R.layout.simple_spinner_item);  //create the adapter for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //set its resources to our amounts list resource
        newAmount.setAdapter(adapter);  //give the adapter to the spinner
        newAmount.setOnItemSelectedListener(this);  //set the listener to the one above
        newAmount.setId(numIngredients + 1);    //update its id for access
        return newAmount;   //return to caller
    }
}

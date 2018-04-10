package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public View baseView;
        public TextView nameTextbox, priceTextbox, ingredientsTextbox;
        public ImageView recipeImage;

        public RecipeViewHolder(View recipeView) {
            super(recipeView);
            this.baseView = recipeView;
            this.nameTextbox = recipeView.findViewById(R.id.name_textbox);
            this.priceTextbox = recipeView.findViewById(R.id.price_textbox);
            this.ingredientsTextbox = recipeView.findViewById(R.id.ingredients_textbox);
            this.recipeImage = recipeView.findViewById(R.id.recipe_image);
        }
    }

    private final LayoutInflater recipeInflator;
    private List<Recipe> recipeList;

    public RecipeAdapter(Context context) {
        this.recipeInflator = LayoutInflater.from(context);
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView recipeView = (CardView) this.recipeInflator.inflate(R.layout.recipe_cardview, parent, false);
        return new RecipeAdapter.RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe thisRecipe = recipeList.get(position);
        final int recipeId = thisRecipe.getId();

        holder.nameTextbox.setText(String.valueOf(thisRecipe.getName()));
        holder.priceTextbox.setText(String.valueOf(thisRecipe.getPrice()));
        holder.ingredientsTextbox.setText(String.valueOf(thisRecipe.getIngredients()));

        String recipePicture = thisRecipe.getPictureId();
        if (recipePicture != null) {
            Bitmap userSelectedImage = BitmapFactory.decodeFile(thisRecipe.getPictureId());
            holder.recipeImage.setImageBitmap(userSelectedImage);
        }

        holder.baseView.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View recipeView) {
                Context context = recipeView.getContext();
                Intent intent = new Intent(context, RecipeViewActivity.class);
                intent.putExtra("recipeId", recipeId);
                context.startActivity(intent);
            }
        });
    }

    void setRecipeList(List<Recipe> recipeList){
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 : recipeList.size();
    }
}
package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        public TextView positionTextbox, ingredientTextbox, measureTextbox, measureTypeTextbox;

        public IngredientViewHolder(View ingredientView) {
            super(ingredientView);
            this.positionTextbox = ingredientView.findViewById(R.id.position_textbox);
            this.ingredientTextbox = ingredientView.findViewById(R.id.ingredient_name_textbox);
            this.measureTextbox = ingredientView.findViewById(R.id.measure_textbox);
            this.measureTypeTextbox = ingredientView.findViewById(R.id.measure_type_textbox);
        }
    }

    private final LayoutInflater ingredientInflator;
    private String[] ingredientList;

    public IngredientAdapter(Context context) {
        this.ingredientInflator = LayoutInflater.from(context);
    }

    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView ingredientView = (CardView) this.ingredientInflator.inflate(R.layout.ingredient_cardview, parent, false);
        return new IngredientAdapter.IngredientViewHolder(ingredientView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        String thisIngredient = ingredientList[position];
        String[] thisIngredientSplit = thisIngredient.split(",");
        holder.positionTextbox.setText(String.valueOf(position + 1));
        holder.ingredientTextbox.setText(thisIngredientSplit[0]);
        if (thisIngredientSplit.length > 1)
        {
            holder.measureTextbox.setText(thisIngredientSplit[1]);
        }
    }

    void setIngredientList(String[] ingredientList){
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredientList == null ? 0 : ingredientList.length;
    }
}

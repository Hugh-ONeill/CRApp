package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.DirectionViewHolder> {

    public static class DirectionViewHolder extends RecyclerView.ViewHolder {
        public TextView positionTextbox, directionTextbox;

        public DirectionViewHolder(View directionView) {
            super(directionView);
            this.positionTextbox = directionView.findViewById(R.id.position_textbox);
            this.directionTextbox = directionView.findViewById(R.id.direction_textbox);
        }
    }

    private final LayoutInflater directionInflator;
    private List<String> directionList;

    public DirectionAdapter(Context context) {
        this.directionInflator = LayoutInflater.from(context);
    }

    @Override
    public DirectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView directionView = (CardView) this.directionInflator.inflate(R.layout.direction_cardview, parent, false);
        return new DirectionViewHolder(directionView);
    }

    @Override
    public void onBindViewHolder(DirectionViewHolder holder, int position) {
        String thisDirection = directionList.get(position);
        holder.positionTextbox.setText(String.valueOf(position + 1));
        holder.directionTextbox.setText(thisDirection);
    }

    void setDirectionList(List<String> directionList){
        this.directionList = directionList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return directionList == null ? 0 : directionList.size();
    }
}


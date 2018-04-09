package edu.pitt.cs.cs1635.group8.collegerecipeapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String DB_NAME = "Database.db";
    private static volatile RecipeDatabase INSTANCE;

    static synchronized RecipeDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = create(context);
                }
            }
        }
        return INSTANCE;
    }

    private static RecipeDatabase create(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class, DB_NAME).build();
    }

    public abstract RecipeDao getRecipeDao();
}

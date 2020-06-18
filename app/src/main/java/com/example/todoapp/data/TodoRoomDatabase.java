package com.example.todoapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.todoapp.model.Todo;

import java.util.Date;


@Database(entities = {Todo.class}, version = 3, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();


    private static TodoRoomDatabase INSTANCE;

    public static TodoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class, "todo_database")

                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final TodoDao mDao;

        PopulateDbAsync(TodoRoomDatabase db) {
            mDao = db.todoDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            if (mDao.getAnyTodoItem().length < 1) {
                Todo todo = new Todo("Add a Todo Item",
                        "Add a description of the Todo item to get started.",
                        new Date(),
                        false,
                        1);
                mDao.insert(todo);
            }
            return null;
        }
    }
}

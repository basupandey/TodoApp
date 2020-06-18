package com.example.todoapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.todoapp.model.Todo;

import java.util.List;


@Dao
public interface TodoDao {

    @Insert
    void insert(Todo todo);


    @Query("DELETE FROM todo_table")
    void deleteAll();


    @Query("SELECT * FROM todo_table ORDER BY todo_date, priority, is_complete  ASC")
    LiveData<List<Todo>> getAllTodos();

    @Query("SELECT * FROM todo_table LIMIT 1")
    Todo[] getAnyTodoItem();


    @Delete
    void deleteTodo(Todo todo);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Todo... todo);

    @Query("SELECT * FROM todo_table WHERE id = :todo_id")
    LiveData<Todo> getTodoWithId(int todo_id);


    @Query("DELETE FROM todo_table WHERE is_complete = 1")
    void deleteCompletedTodos();
}

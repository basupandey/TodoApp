package com.example.todoapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.todoapp.data.TodoRepository;
import com.example.todoapp.model.Todo;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mRepository;

    private LiveData<List<Todo>> mAllTodos;


    public TodoViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }


    public LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }


    public void insert(Todo todo) {
        mRepository.insert(todo);
    }


    public void deleteAll() {
        mRepository.deleteAll();
    }


    public void deleteTodo(Todo todo){
        mRepository.deleteTodo(todo);
    }


    public void updateTodo(Todo todo) {
        mRepository.updateTodo(todo);
    }


    public void deleteCompletedTodos() {
        mRepository.deleteCompletedTodos();
    }


}

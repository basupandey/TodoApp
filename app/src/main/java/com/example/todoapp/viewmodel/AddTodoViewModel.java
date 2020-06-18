package com.example.todoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.todoapp.data.TodoRepository;
import com.example.todoapp.model.Todo;

public class AddTodoViewModel extends ViewModel {

    private TodoRepository mTodoRepository;
    private LiveData<Todo> todo;

    public AddTodoViewModel(TodoRepository todoRepository, int todoId) {
        this.mTodoRepository = todoRepository;
        todo = mTodoRepository.getTodo(todoId);
    }

    public LiveData<Todo> getTodo() { return todo; }
}

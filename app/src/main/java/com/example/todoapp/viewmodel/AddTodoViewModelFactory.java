package com.example.todoapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.todoapp.data.TodoRepository;


public class AddTodoViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final TodoRepository todoRepository;
    private final int mTodoId;

    public AddTodoViewModelFactory(TodoRepository todoRepository, int mTodoId) {

        this.todoRepository = todoRepository;
        this.mTodoId = mTodoId;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTodoViewModel(todoRepository, mTodoId);
    }
}

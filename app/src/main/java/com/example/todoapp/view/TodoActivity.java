package com.example.todoapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.todoapp.R;

public class TodoActivity extends AppCompatActivity {

    private static final String EXTRA_TODO_ID = "com.example.todoapp.TODO_ID";

    public static Intent newIntent(Context packageContext, int todoId) {
        Intent intent = new Intent(packageContext, TodoActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            int todoId = getIntent().getIntExtra(EXTRA_TODO_ID, -1);

            TodoFragment todoFragment = TodoFragment.newInstance(todoId);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, todoFragment)
                    .commit();
        }
    }
}

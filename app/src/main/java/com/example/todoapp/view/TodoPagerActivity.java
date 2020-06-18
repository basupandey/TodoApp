package com.example.todoapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.todoapp.R;
import com.example.todoapp.viewmodel.TodoViewModel;
import com.example.todoapp.model.Todo;

import java.util.List;

public class TodoPagerActivity extends AppCompatActivity {


    private static final String EXTRA_TODO_ID = "com.example.todoapp.TODO_ID";

    private ViewPager mViewPager;
    private List<Todo> mTodos = null;
    private TodoViewModel mTodoViewModel;

    public static Intent newIntent(Context packageContext, int todoId) {
        Intent intent = new Intent(packageContext, TodoPagerActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_pager);

        final int todoId = getIntent().getIntExtra(EXTRA_TODO_ID, -1);


        mViewPager = findViewById(R.id.todo_view_pager);


        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Todo todo = mTodos.get(position);
                Log.d("TODOPAGER", "" + todo.getId());
                return TodoFragment.newInstance(todo.getId());
            }

            @Override
            public int getCount() {
                if (mTodos == null)
                    return 0;
                return mTodos.size();
            }
        };


        mViewPager.setAdapter(fragmentStatePagerAdapter);


        mTodoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            // onChanged() method invoked when  observed data changes, or when the app opens initially
            @Override
            public void onChanged(@Nullable List<Todo> todos) {

                mTodos = todos;

                fragmentStatePagerAdapter.notifyDataSetChanged();

                for (int i = 0; i < mTodos.size(); i++) {
                    if (mTodos.get(i).getId() == todoId) {
                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }


            }
        });
    }
}

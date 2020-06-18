package com.example.todoapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todoapp.R;
import com.example.todoapp.viewmodel.TodoViewModel;
import com.example.todoapp.model.Todo;

import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.example.todoapp.view.TodoFragment.DEFAULT_TODO_ID;

public class TodoListFragment extends Fragment {
    private TodoViewModel mTodoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = TodoActivity.newIntent(getActivity(), DEFAULT_TODO_ID);
                startActivity(intent);
            }
        });


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        final TodoListAdapter adapter = new TodoListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        mTodoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            // onChanged() method invoked when  observed data changes, or when the app opens initially
            @Override
            public void onChanged(@Nullable List<Todo> todos) {

                adapter.setTodos(todos);
            }
        });


        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Todo todo = adapter.getTodoAtPosition(position);
                        Toasty.success(getActivity(), "Deleted: " + todo.getTitle(), Toast.LENGTH_SHORT, true).show();



                        mTodoViewModel.deleteTodo(todo);
                    }
                }
        );
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TodoListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Todo todo = adapter.getTodoAtPosition(position);
                launchUpdateTodoActivity(todo);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id) {
            case R.id.clear_data:

                mTodoViewModel.deleteAll();

                Toasty.success(getActivity(),
                        "Cleared all Todos",
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.clear_completed_todos:

                mTodoViewModel.deleteCompletedTodos();

                Toasty.success(getActivity(),
                        "Cleared all completed Todos",
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_item_share:
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                mTodoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
                    // onChanged() method invoked when  observed data changes, or when the app opens initially
                    @Override
                    public void onChanged(@Nullable List<Todo> todos) {
                        String todosToShare = "";
                        for (int i = 0; i < todos.size(); i++) {
                            todosToShare += todos.get(i).getTitle() + "\n\n";
                        }


                        String shareBody = todosToShare;
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    }
                });

                try {
                    startActivity(Intent.createChooser(intent, "Select Application To Share"));
                } catch (android.content.ActivityNotFoundException ex) {

                    Log.e("IntentError", ex.toString());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void launchUpdateTodoActivity(Todo todo) {


        Intent intent = TodoPagerActivity.newIntent(getActivity(), todo.getId());
        startActivity(intent);
    }
}

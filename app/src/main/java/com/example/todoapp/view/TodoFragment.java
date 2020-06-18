package com.example.todoapp.view;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.todoapp.viewmodel.AddTodoViewModel;
import com.example.todoapp.viewmodel.AddTodoViewModelFactory;
import com.example.todoapp.R;
import com.example.todoapp.data.TodoRepository;
import com.example.todoapp.viewmodel.TodoViewModel;
import com.example.todoapp.model.Todo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class TodoFragment extends Fragment {

    private static final String ARG_TODO_ID = "com.example.todoapp.TODO_ID";

    public static final String INSTANCE_TODO_ID = "instanceTodoId";
    public static final String INSTANCE_TODO_SELECTED_DATE = "instanceTodoSelectedDate";


    static final int DEFAULT_TODO_ID = -1;

    private LiveData<Todo> mTodo;


    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mTodoDateButton;
    private CheckBox mCompletedCheckbox;
    private Button deleteButton;
    public RadioGroup mPriorityRadioGroup;


    private int mTodoId = DEFAULT_TODO_ID;


    private TodoViewModel mTodoViewModel;


    private AddTodoViewModelFactory addTodoViewModelFactory;
    private AddTodoViewModel addTodoViewModel;

    public static final int HIGH_PRIORITY = 1;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;


    private ShareActionProvider mTodoShareActionProvider;

    public static TodoFragment newInstance(int todoId) {
        Bundle args = new Bundle();
        args.putInt(ARG_TODO_ID, todoId);

        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);


        mTitleEditText = (EditText) view.findViewById(R.id.todo_title);
        mDescriptionEditText = (EditText) view.findViewById(R.id.todo_description);
        mTodoDateButton = (Button) view.findViewById(R.id.todo_date);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTodoDateButton.setText(dateFormat.format(new Date()));
        mCompletedCheckbox = view.findViewById(R.id.todo_complete);
        mPriorityRadioGroup = view.findViewById(R.id.priorityGroup);


        mTodoDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date Picker");
            }
        });


        final Button saveButton = view.findViewById(R.id.button_save);

        deleteButton = view.findViewById(R.id.button_delete);



        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TODO_ID)) {
            mTodoId = savedInstanceState.getInt(INSTANCE_TODO_ID, DEFAULT_TODO_ID);


            if (addTodoViewModel == null) {
                this.addTodoViewModelFactory = new AddTodoViewModelFactory(new TodoRepository(new Application()), mTodoId);
                this.addTodoViewModel = ViewModelProviders.of(this, addTodoViewModelFactory)
                        .get(AddTodoViewModel.class);
            }


            addTodoViewModel.getTodo().observe(getActivity(), new Observer<Todo>() {
                @Override
                public void onChanged(@Nullable Todo todo) {
                    mTodo = addTodoViewModel.getTodo();
                    addTodoViewModel.getTodo().removeObserver(this);
                }
            });



            if (savedInstanceState.containsKey(INSTANCE_TODO_SELECTED_DATE))
                mTodoDateButton.setText(savedInstanceState.getString(INSTANCE_TODO_SELECTED_DATE));
        }


        if (getArguments().containsKey(ARG_TODO_ID)) {
            if (mTodoId == DEFAULT_TODO_ID) {
                mTodoId = getArguments().getInt(ARG_TODO_ID, DEFAULT_TODO_ID);

                         this.addTodoViewModelFactory = new AddTodoViewModelFactory(new TodoRepository(new Application()), mTodoId);
                this.addTodoViewModel = ViewModelProviders.of(this, addTodoViewModelFactory)
                        .get(AddTodoViewModel.class);

                addTodoViewModel.getTodo().observe(getActivity(), new Observer<Todo>() {
                    @Override
                    public void onChanged(@Nullable Todo todo) {
                        mTodo = addTodoViewModel.getTodo();
                        addTodoViewModel.getTodo().removeObserver(this);
                        populateUI(todo);
                    }
                });
            }
        }


        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);



        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String title = mTitleEditText.getText().toString();

                String description = mDescriptionEditText.getText().toString();

                String todoDateString = mTodoDateButton.getText().toString();
                Date todoDate;
                try {
                    DateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    todoDate = (Date) formatter.parse(todoDateString);
                } catch (ParseException e) {
                    todoDate = null;
                    e.printStackTrace();
                }

                boolean isCompleted = mCompletedCheckbox.isChecked();

                int priority = 1;
                int checkedPriorityId = mPriorityRadioGroup.getCheckedRadioButtonId();
                switch (checkedPriorityId) {
                    case R.id.highPriorityButton:
                        priority = HIGH_PRIORITY;
                        break;
                    case R.id.mediumPriorityButton:
                        priority = MEDIUM_PRIORITY;
                        break;
                    case R.id.lowPriorityButton:
                        priority = LOW_PRIORITY;
                }


                final Todo todo = new Todo(title, description, todoDate, isCompleted, priority);


                if (mTodoId == DEFAULT_TODO_ID) {
                    mTodoViewModel.insert(todo);
                    Toasty.success(getActivity(),
                            "Todo created successfully",
                            Toast.LENGTH_LONG).show();
                }

                else {

                    todo.setId(mTodoId);

                    mTodoViewModel.updateTodo(todo);
                    Toasty.success(getActivity(),
                            R.string.todo_updated,
                            Toast.LENGTH_LONG).show();
                }
                getActivity().finish();
            }
        });



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodoViewModel.deleteTodo(mTodo.getValue());
                Toasty.success(getActivity(), "Deleted: " + mTodo.getValue().getTitle(),
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mTodoId != DEFAULT_TODO_ID) {
            inflater.inflate(R.menu.menu_todo, menu);


            MenuItem item = menu.findItem(R.id.menu_item_share_todo);
            mTodoShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

            final Intent todoDetailSharingIntent = new Intent(Intent.ACTION_SEND);


            todoDetailSharingIntent.setType("text/plain");
            todoDetailSharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Todos");


            if (addTodoViewModel == null) {
                this.addTodoViewModelFactory = new AddTodoViewModelFactory(new TodoRepository(new Application()), mTodoId);
                this.addTodoViewModel = ViewModelProviders.of(this, addTodoViewModelFactory)
                        .get(AddTodoViewModel.class);
            }


            addTodoViewModel.getTodo().observe(getActivity(), new Observer<Todo>() {
                @Override
                public void onChanged(@Nullable Todo todo) {
                    addTodoViewModel.getTodo().removeObserver(this);


                    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
                    String todoDueDate = dateFormat.format(todo.getTodoDate());
                    String todoPriority = "Low";
                    switch (todo.getPriority()) {
                        case 1:
                            todoPriority = "High";
                            break;
                        case 2:
                            todoPriority = "Medium";
                            break;
                        case 3:
                            todoPriority = "Low";
                    }

                    String shareBody = todo.getTitle() + "\n\n" +
                            todo.getDescription() + "\n\n" +
                            "Due Date: " + todoDueDate + "\n\n" +
                            "Priority: " + todoPriority;

                    todoDetailSharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                }
            });


            mTodoShareActionProvider.setShareIntent(todoDetailSharingIntent);
        }

    }

    private void populateUI(Todo todo) {
        if (todo == null) {

            deleteButton.setVisibility(View.GONE);
            return;
        }

        mTitleEditText.setText(todo.getTitle());
        mDescriptionEditText.setText(todo.getDescription());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTodoDateButton.setText(dateFormat.format(todo.getTodoDate()));
        mCompletedCheckbox.setChecked(todo.isIsComplete());

        switch (todo.getPriority()) {
            case HIGH_PRIORITY:
                mPriorityRadioGroup.check(R.id.highPriorityButton);
                break;
            case MEDIUM_PRIORITY:
                mPriorityRadioGroup.check(R.id.mediumPriorityButton);
                break;
            case LOW_PRIORITY:
                mPriorityRadioGroup.check(R.id.lowPriorityButton);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt(INSTANCE_TODO_ID, mTodoId);
        String selectedDate = mTodoDateButton.getText().toString();
        outState.putString(INSTANCE_TODO_SELECTED_DATE, selectedDate);
        super.onSaveInstanceState(outState);
    }
}

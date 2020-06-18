package com.example.todoapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.todoapp.R;
import com.example.todoapp.model.Todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {
    private final LayoutInflater mInflater;
    private List<Todo> mTodos;
    private static ClickListener clickListener;


    private String HIGH_PRIORITY_COLOR_CODE = "#EA0A0A";
    private String MEDIUM_PRIORITY_COLOR_CODE = "#EA950A";
    private String LOW_PRIORITY_COLOR_CODE = "#DEDE11";


    private String TODO_COMPLETED_COLOR_CODE = "#11DE2B";


    TodoListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_todo, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoViewHolder holder, int position) {
        if (mTodos != null ) {
            Todo current = mTodos.get(position);
            holder.todoItemView.setText(current.getTitle());


            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
            holder.todoDateItemView.setText(dateFormat.format(current.getTodoDate()));
            LinearLayout linearLayoutViewGroup = (LinearLayout) ((ViewGroup) holder.todoItemView.getParent());


            switch (current.getPriority()){
                case 1:
                    linearLayoutViewGroup.setBackgroundColor(Color.parseColor(HIGH_PRIORITY_COLOR_CODE));
                    break;
                case 2:
                    linearLayoutViewGroup.setBackgroundColor(Color.parseColor(MEDIUM_PRIORITY_COLOR_CODE));
                    break;
                case 3:
                    linearLayoutViewGroup.setBackgroundColor(Color.parseColor(LOW_PRIORITY_COLOR_CODE));
            }


            if (current.isIsComplete()) {
                linearLayoutViewGroup.setBackgroundColor(Color.parseColor(TODO_COMPLETED_COLOR_CODE));
                holder.todoItemView.setPaintFlags(holder.todoItemView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                holder.todoItemView.setPaintFlags( holder.todoItemView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
        } else {

            holder.todoItemView.setText("No Todo");
            holder.todoItemView.setText("No Todo Date");
        }
    }

    void setTodos(List<Todo> todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (mTodos != null)
            return mTodos.size();
        else
            return 0;
    }


    class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TextView todoItemView;
        private final TextView todoDateItemView;

        private TodoViewHolder(View itemView) {
            super(itemView);
            todoItemView = itemView.findViewById(R.id.recyclerview_todo_title);
            todoDateItemView = itemView.findViewById(R.id.recyclerview_todo_date);

            todoItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public Todo getTodoAtPosition (int position) {
        return mTodos.get(position);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TodoListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}

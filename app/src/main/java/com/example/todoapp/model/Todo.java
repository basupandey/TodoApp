package com.example.todoapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.todoapp.util.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "todo_table")
public class Todo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "todo_date")
    @TypeConverters({DateConverter.class})
    private Date mTodoDate;

    @ColumnInfo(name = "is_complete")
    private boolean mIsComplete;

    @ColumnInfo(name = "priority")
    private int mPriority;


    @Ignore
    public Todo() {}

    @Ignore
    public Todo(int id, @NonNull String title, String description, Date todoDate, boolean isComplete, int priority) {
        this.id = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mTodoDate = todoDate;
        this.mIsComplete = isComplete;
        this.mPriority = priority;
    }

    public Todo(@NonNull String title, String description, Date todoDate, boolean isComplete, int priority) {
        this.mTitle = title;
        this.mDescription = description;
        this.mTodoDate = todoDate;
        this.mIsComplete = isComplete;
        this.mPriority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Date getTodoDate() {
        return mTodoDate;
    }

    public boolean isIsComplete() {
        return mIsComplete;
    }

    public int getPriority() { return mPriority; }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NonNull String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setTodoDate(Date mTodoDate) {
        this.mTodoDate = mTodoDate;
    }

    public void setIsComplete(boolean mIsComplete) {
        this.mIsComplete = mIsComplete;
    }

    public void setPriority(int mPriority) { this.mPriority = mPriority; }


}

package com.example.todoapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.todoapp.model.Todo;

import java.util.List;

public class TodoRepository {
    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllTodos;


    public TodoRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodos = mTodoDao.getAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    public LiveData<Todo> getTodo(int id) {
        return mTodoDao.getTodoWithId(id);
    };

    public void insert(Todo todo) {

        new insertAsyncTask(mTodoDao).execute(todo);
    }

    public void deleteAll() {

        new deleteAllTodosAsyncTask(mTodoDao).execute();
    }

    public void deleteTodo(Todo todo) {

        new deleteTodoASyncTask(mTodoDao).execute(todo);
    }

    public void updateTodo(Todo todo) {

        new updateTodoAsyncTask(mTodoDao).execute(todo);
    }

    public void deleteCompletedTodos() {

        new deleteCompletedTodosAsyncTask(mTodoDao).execute();
    }




    private static class insertAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;

        insertAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {

            Log.d("TODO REPOSITORY", "" + params[0]);
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class deleteAllTodosAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoDao mAsyncTaskDao;

        deleteAllTodosAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    private static class deleteTodoASyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;

        deleteTodoASyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.deleteTodo(params[0]);
            return null;
        }
    }


    private static class updateTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mAsyncTaskDao;

        updateTodoAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Todo... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }


    private static class deleteCompletedTodosAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoDao mAsyncTaskDao;

        deleteCompletedTodosAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteCompletedTodos();
            return null;
        }
    }


}

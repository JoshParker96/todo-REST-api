package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {
  Todo findTodoById(int id) throws DaoException;

  void addTodo(Todo todo) throws DaoException;

  List<Todo> fetchAllTodos() throws DaoException;

  void updateTodo(Todo todo) throws DaoException;

  void deleteTodo(Todo todo) throws DaoException;
}

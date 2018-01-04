package com.teamtreehouse.techdegrees;

import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFileLocation;

import com.google.gson.Gson;

import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exc.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

public class Api {

  public static void main(String[] args) {

    staticFileLocation("/public");

    String dataSource = "jdbc:h2:./todos.db";
    String
        connectionString =
        String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", dataSource);
    Sql2o sql20 = new Sql2o(connectionString, "", "");
    TodoDao todoDao = new Sql2oTodoDao(sql20);
    Gson gson = new Gson();

    get("/api/v1/todos", "application/json", (req, res) -> todoDao.fetchAllTodos(), gson::toJson);

    post("/api/v1/todos", "application/json", ((req, res) -> {
      Todo todo = gson.fromJson(req.body(), Todo.class);
      todoDao.addTodo(todo);
      res.status(201);
      return todoDao.fetchAllTodos();
    }), gson::toJson);

    put("/api/v1/todos/:id", "application/json", ((req, res) -> {
      int id = Integer.parseInt(req.params("id"));
      String name = gson.fromJson(req.body(), Todo.class).getName();
      boolean edited = gson.fromJson(req.body(), Todo.class).isEdited();
      boolean completed = gson.fromJson(req.body(), Todo.class).isCompleted();
      Todo todo = new Todo(id, name, edited, completed);
      todoDao.updateTodo(todo);
      res.status(201);
      return todoDao.fetchAllTodos();
    }), gson::toJson);

    delete("/api/v1/todos/:id", "application/json", ((req, res) -> {
      int id = Integer.parseInt(req.params("id"));
      Todo todo = todoDao.findTodoById(id);
      if (todo == null) {
        throw new ApiError("Could not find TODO with id", 404);
      }
      todoDao.deleteTodo(todo);
      res.status(201);
      return todoDao.fetchAllTodos();
    }), gson::toJson);

    after((req, res) -> res.type("application/json"));

    exception(ApiError.class, (exc, req, res) -> {
      ApiError error = (ApiError) exc;
      Map<String, Object> jsonMap = new HashMap<>();
      jsonMap.put("status", error.getStatus());
      jsonMap.put("errorMessage", error.getMessage());
      res.type("application/json");
      res.status(error.getStatus());
      res.body(gson.toJson(jsonMap));
    });
  }
}

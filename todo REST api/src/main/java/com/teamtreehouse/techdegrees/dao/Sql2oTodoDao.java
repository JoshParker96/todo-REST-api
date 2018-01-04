package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao {

  private final Sql2o SQL2O;

  public Sql2oTodoDao(Sql2o sql2o) {
    this.SQL2O = sql2o;
  }

  @Override
  public Todo findTodoById(int id) throws DaoException {
    String sql = String.format("SELECT * FROM todos WHERE id = %s", id);
    try (Connection connection = SQL2O.open()) {
      return connection.createQuery(sql)
          .executeAndFetchFirst(Todo.class);
    } catch (Sql2oException exception) {
      throw new DaoException(exception, "Problem finding TODO");
    }
  }

  @Override
  public void addTodo(Todo todo) throws DaoException {
    String sql = String.format("INSERT INTO todos(name, edited, completed)"
        + "VALUES(:name, :edited, :completed)");
    try (Connection connection = SQL2O.open()) {
      int id = (int) connection.createQuery(sql)
          .bind(todo)
          .executeUpdate()
          .getKey();
      todo.setId(id);
    } catch (Sql2oException exception) {
      throw new DaoException(exception, "Problem adding TODO");
    }
  }

  @Override
  public List<Todo> fetchAllTodos() throws DaoException {
    String sql = "SELECT * FROM todos";
    try (Connection con = SQL2O.open()) {
      return con.createQuery(sql)
          .executeAndFetch(Todo.class);
    } catch (Sql2oException ex) {
      throw new DaoException(ex, "Problem retrieving all TODOs");
    }
  }

  @Override
  public void updateTodo(Todo todo) throws DaoException {
    String sql =
        String.format("UPDATE todos SET name = :name, edited = :edited, completed = :completed WHERE id = :id");
    try (Connection connection = SQL2O.open()) {
      connection.createQuery(sql)
          .addParameter("id", todo.getId())
          .addParameter("name", todo.getName())
          .addParameter("completed", todo.isCompleted())
          .addParameter("edited", todo.isEdited())
          .executeUpdate();
    } catch (Sql2oException exception) {
      throw new DaoException(exception, "Problem updating TODO");
    }
  }

  @Override
  public void deleteTodo(Todo todo) throws DaoException {
    String sql = String.format("DELETE FROM todos WHERE id = :id");
    try (Connection connection = SQL2O.open()) {
      connection.createQuery(sql)
          .addParameter("id", todo.getId())
          .executeUpdate();
    } catch (Sql2oException exception) {
      throw new DaoException(exception, "Problem deleting TODO");
    }
  }
}

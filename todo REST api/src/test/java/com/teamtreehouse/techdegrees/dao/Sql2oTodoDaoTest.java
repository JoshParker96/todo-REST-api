package com.teamtreehouse.techdegrees.dao;

import static org.junit.Assert.assertEquals;

import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Sql2oTodoDaoTest {
  private Sql2oTodoDao dao;
  private Connection conn;

  @Before
  public void setUp() throws Exception {
    String
        connectionString =
        String.format("jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'");
    Sql2o sql2o = new Sql2o(connectionString, "", "");
    dao = new Sql2oTodoDao(sql2o);
    conn = sql2o.open();
  }

  @After
  public void tearDown() throws Exception {
    conn.close();
  }

  @Test
  public void creatingTodoSetsId() throws Exception {
    Todo todo = new Todo("josh", false, true);
    int originalTodoId = todo.getId();

    dao.addTodo(todo);

    assertEquals(originalTodoId, 0);
  }

  @Test
  public void addedTodosAreReturnedFromFetchAllTodos() throws Exception {
    Todo todo = new Todo("josh", false, true);

    dao.addTodo(todo);

    assertEquals(1, dao.fetchAllTodos().size());
  }
}
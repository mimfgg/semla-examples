package io.semla.examples.graphql.health;

import com.codahale.metrics.health.HealthCheck;
import io.semla.datasource.Datasource;
import io.semla.examples.graphql.model.Book;
import javax.inject.Inject;

public class DatasourceHealthCheck extends HealthCheck {

  private final Datasource<Book> books;

  @Inject
  public DatasourceHealthCheck(Datasource<Book> books) {
    this.books = books;
  }

  @Override
  protected Result check() {
    try {
      books.count();
    } catch (Exception e) {
      return Result.unhealthy("books datasource returned: " + e.getMessage());
    }
    return Result.healthy();
  }
}
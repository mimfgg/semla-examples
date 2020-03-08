package io.semla.examples.mysql;

import io.semla.examples.mysql.model.AuthorManager;
import io.semla.examples.mysql.model.BookManager;
import javax.inject.Inject;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Library {

  @Inject
  private BookManager books;

  @Inject
  private AuthorManager authors;

}

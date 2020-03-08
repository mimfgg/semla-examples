package io.semla.examples.mysql;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import io.semla.Semla;
import io.semla.datasource.MysqlDatasource;
import io.semla.examples.mysql.model.Book;
import io.semla.logging.Logging;
import io.semla.serialization.yaml.Yaml;
import io.semla.util.Lists;
import java.util.Optional;
import java.util.TimeZone;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

@Slf4j
public class LibraryTest {

  static {
    Logging.setTo(Level.INFO);
  }

  @ClassRule
  public static JdbcDatabaseContainer<?> container = new MySQLContainer<>("mysql:5.6");

  @Inject
  private Library library;

  @Before
  public void before() {
    Semla.configure()
        .withDefaultDatasource(MysqlDatasource.configure()
            .withJdbcUrl(
                container.getJdbcUrl() + "?serverTimezone=" + TimeZone.getDefault().getID())
            .withUsername(container.getUsername())
            .withPassword(container.getPassword())
            .withAutoCreateTable(true))
        .create()
        .inject(this);
  }

  @Test
  public void testLibrary() {
    Logging.setTo(Level.DEBUG);
    library.authors()
        .newAuthor("J.R.R Tolkien")
        .books(Lists.of(Book.builder().name("Bilbo the hobbit").build()))
        .create();

    Optional<Book> bilboTheHobbit = library.books()
        .where().name().is("Bilbo the hobbit")
        .first(book -> book.author(author -> author.books()));

    assertThat(bilboTheHobbit).isPresent();
    assertThat(bilboTheHobbit.get().author.name).isEqualTo("J.R.R Tolkien");

    log.info("for information:\n" + Yaml.write(bilboTheHobbit));
  }
}

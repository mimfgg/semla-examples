package io.semla.examples.mysql;

import ch.qos.logback.classic.Level;
import io.semla.Semla;
import io.semla.datasource.MysqlDatasource;
import io.semla.examples.mysql.model.Book;
import io.semla.logging.Logging;
import io.semla.serialization.yaml.Yaml;
import io.semla.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import javax.inject.Inject;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LibraryTest {

    static {
        Logging.withAppenderLevel("io.semla", Level.DEBUG).withLogLevel(Level.INFO).setup();
    }

    @ClassRule
    public static JdbcDatabaseContainer<?> container = new MySQLContainer<>("mysql:5.6");

    @Inject
    private Library library;

    @Before
    public void before() {
        if (library == null) {
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
    }

    @Test
    public void testLibrary() {
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

package io.semla.examples.graphql;

import ch.qos.logback.classic.Level;
import com.decathlon.tzatziki.steps.HttpSteps;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.semla.datasource.PostgresqlDatasource;
import io.semla.examples.graphql.config.GraphQLConfiguration;
import io.semla.logging.Logging;
import io.semla.util.Maps;
import org.junit.runner.RunWith;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(Cucumber.class)
@SuppressWarnings("ALL")
@CucumberOptions(plugin = "pretty", glue = {"com.decathlon.tzatziki.steps", "io.semla.examples.graphql"})
public class GraphQLApplicationTest {

    public static class Steps {

        private static final JdbcDatabaseContainer<?> db =
            new PostgreSQLContainer<>("postgres:12.1")
                .withTmpFs(Maps.of("/var/lib/postgresql/data", "rw"));

        private static DropwizardAppRule<GraphQLConfiguration> server;

        private final HttpSteps http;

        public Steps(HttpSteps http) {
            this.http = http;
        }

        @Before
        public void before() throws Exception {
            if (server == null) {
                db.start();
                server = new DropwizardAppRule<>(GraphQLApplication.class,
                    GraphQLConfiguration.builder()
                        .datasource(PostgresqlDatasource.configure()
                            .withJdbcUrl(db.getJdbcUrl())
                            .withUsername(db.getUsername())
                            .withPassword(db.getPassword())
                            .withAutoCreateTable(true))
                        .build());
            }
            server.getTestSupport().before();
            Logging.withAppenderLevel("io.semla", Level.DEBUG).setup();
            http.setRelativeUrlRewriter(path -> "http://localhost:%s%s".formatted(server.getLocalPort(), path));
        }

        @After
        public void after() {
            server.getTestSupport().after();
        }
    }
}

package io.semla.examples.graphql;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.semla.datasource.PostgresqlDatasource;
import io.semla.examples.graphql.config.GraphQLConfiguration;
import io.semla.logging.Logging;
import io.semla.serialization.json.Json;
import io.semla.serialization.json.JsonSerializer;
import io.semla.util.Maps;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty")
public class GraphQLApplicationTest {

  public static class Steps {

    private static final JdbcDatabaseContainer<?> db =
        new PostgreSQLContainer<>("postgres:12.1")
            .withTmpFs(Maps.of("/var/lib/postgresql/data", "rw"));

    private static DropwizardAppRule<GraphQLConfiguration> server;

    private Response response;

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
    }

    @After
    public void after() {
      server.getTestSupport().after();
    }

    @Given("^(?:that )?we post on \"([^\"]*)\":$")
    public void that_we_post_on(String path, String content) {
      response = server.client().target(
          String.format("http://localhost:%d%s", server.getLocalPort(), path))
          .request()
          .post(Entity.text(content));
      if (response.getStatus() != 200) {
        Assert.fail("received: " + response.readEntity(String.class));
      }
    }

    @Then("^we receive:$")
    public void we_receive(String content) {
      String entity = response.readEntity(String.class);
      assertThat(entity).isNotNull().isNotBlank();
      assertThat(Json.write(Json.read(entity), JsonSerializer.PRETTY)).isEqualTo(content);
    }
  }
}

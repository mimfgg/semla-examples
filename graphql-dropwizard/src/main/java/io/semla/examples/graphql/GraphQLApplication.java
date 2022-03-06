package io.semla.examples.graphql;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.semla.Semla;
import io.semla.datasource.Datasource;
import io.semla.datasource.PostgresqlDatasource;
import io.semla.examples.graphql.api.GraphQLResource;
import io.semla.examples.graphql.config.GraphQLConfiguration;
import io.semla.examples.graphql.health.DatasourceHealthCheck;
import io.semla.examples.graphql.model.Author;
import io.semla.examples.graphql.model.Book;
import io.semla.inject.GraphQLModule;
import io.semla.serialization.jackson.MixIns;

public class GraphQLApplication extends Application<GraphQLConfiguration> {

  public static void main(String[] args) throws Exception {
    new GraphQLApplication().run(args);
  }

  @Override
  public void initialize(final Bootstrap<GraphQLConfiguration> bootstrap) {
    // Semla's TypeInfo works a bit differently than Jackson's
    // Therefore we need to register a mixIn for jackson to be able to compose both types
    bootstrap.getObjectMapper().addMixIn(
        Datasource.Configuration.class,
        MixIns.createFor(PostgresqlDatasource.Configuration.class)
    );
  }

  @Override
  public void run(GraphQLConfiguration configuration, Environment environment) {
    Semla semla = Semla.configure()
        .withDatasourceOf(Book.class, Author.class).as(configuration.getDatasource())
        .withModules(new GraphQLModule())
        .create();
    environment.jersey().register(semla.getInstance(GraphQLResource.class));
    environment.healthChecks()
        .register("datasource", semla.getInstance(DatasourceHealthCheck.class));
  }
}

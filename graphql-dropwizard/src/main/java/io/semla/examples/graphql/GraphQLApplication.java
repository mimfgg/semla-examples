package io.semla.examples.graphql;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.semla.Semla;
import io.semla.config.DatasourceConfiguration;
import io.semla.examples.graphql.api.GraphQLResource;
import io.semla.examples.graphql.config.DatasourceConfigurationMixIn;
import io.semla.examples.graphql.config.GraphQLConfiguration;
import io.semla.examples.graphql.health.DatasourceHealthCheck;
import io.semla.examples.graphql.model.Author;
import io.semla.examples.graphql.model.Book;
import io.semla.inject.GraphQLModule;

public class GraphQLApplication extends Application<GraphQLConfiguration> {

  public static void main(String[] args) throws Exception {
    new GraphQLApplication().run(args);
  }

  @Override
  public void initialize(final Bootstrap<GraphQLConfiguration> bootstrap) {
    bootstrap.getObjectMapper()
        .addMixIn(DatasourceConfiguration.class, DatasourceConfigurationMixIn.class);
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

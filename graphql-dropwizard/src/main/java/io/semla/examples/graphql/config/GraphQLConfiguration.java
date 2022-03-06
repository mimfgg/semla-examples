package io.semla.examples.graphql.config;

import io.dropwizard.Configuration;
import io.semla.datasource.Datasource;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class GraphQLConfiguration extends Configuration {

  private final Datasource.Configuration datasource;

  public Datasource.Configuration getDatasource() {
    return datasource;
  }
}

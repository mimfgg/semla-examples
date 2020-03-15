package io.semla.examples.graphql.config;

import io.dropwizard.Configuration;
import io.semla.config.DatasourceConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLConfiguration extends Configuration {

  private DatasourceConfiguration datasource;

  public DatasourceConfiguration getDatasource() {
    return datasource;
  }

  public void setDatasource(final DatasourceConfiguration datasource) {
    this.datasource = datasource;
  }
}

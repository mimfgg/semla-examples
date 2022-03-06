package io.semla.examples.graphql.api;

import com.codahale.metrics.annotation.Timed;
import graphql.ExecutionResult;
import graphql.GraphQL;
import io.semla.serialization.json.Json;
import io.semla.serialization.json.JsonSerializer;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Path("/graphql")
@Produces(MediaType.APPLICATION_JSON)
public class GraphQLResource {

  private final GraphQL graphQL;

  @Inject
  public GraphQLResource(GraphQL graphQL) {
    this.graphQL = graphQL;
  }

  @POST
  @Timed
  public Object post(String query) {
    ExecutionResult execute = graphQL.execute(query);
    if (execute.isDataPresent()) {
      return execute.getData();
    }
    throw new WebApplicationException(Json.write(execute.getErrors(), JsonSerializer.PRETTY), 500);
  }
}

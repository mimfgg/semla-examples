package io.semla.examples.graphql.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import io.semla.config.PostgresqlDatasourceConfiguration;
import io.semla.reflect.Methods;
import io.semla.reflect.Types;
import io.semla.serialization.json.Json;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Semla's TypeInfo works a bit differently than Jackson's
 * Therefore we need to register a mixIn for jackson to be able to compose both
 * The Deserializer is because of the @Deserialize annotations
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = Void.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PostgresqlDatasourceConfiguration.class, name = "postgresql"),
})
@JsonDeserialize(using = DatasourceConfigurationMixIn.SemlaDeserializer.class)
public interface DatasourceConfigurationMixIn<T> {

  class SemlaDeserializer<T> extends JsonDeserializer<T>
      implements ContextualDeserializer {

    static {
      Types.registerSubType(PostgresqlDatasourceConfiguration.class);
    }

    private Class<T> clazz;

    public SemlaDeserializer() {} // required

    public SemlaDeserializer(Class<T> clazz) {
      this.clazz = clazz;
    }

    @Override
    public T deserialize(final JsonParser jp,
                         final DeserializationContext ctxt)
        throws IOException {
      String type = Methods
          .<JsonNode>invoke(jp.getParsingContext().getParent(), "currentNode")
          .get("type").asText();
      Map<String, Object> asMap = new LinkedHashMap<>();
      asMap.put("type", type);
      while (!jp.currentToken().equals(JsonToken.END_OBJECT)) {
        jp.nextToken();
        asMap.put(jp.currentName(), jp.getValueAsString());
        jp.nextToken();
      }
      // let's write/read it again
      return Json.read(Json.write(asMap), clazz);
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext ctxt,
                                                final BeanProperty property) {
      JavaType type = ctxt.getContextualType() != null
                      ? ctxt.getContextualType()
                      : property.getMember().getType();
      return new SemlaDeserializer<>(type.getRawClass());
    }
  }

}


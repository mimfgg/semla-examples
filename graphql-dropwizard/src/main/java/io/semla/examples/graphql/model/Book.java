package io.semla.examples.graphql.model;

import io.semla.persistence.annotations.Indexed;
import io.semla.persistence.annotations.Managed;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Managed
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

  @Id
  @GeneratedValue
  public int id;

  @Indexed
  @NotNull
  public String name;

  @ManyToOne(fetch = FetchType.LAZY)
  public Author author;
}

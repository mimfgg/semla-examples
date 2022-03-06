package io.semla.examples.graphql.model;

import io.semla.persistence.annotations.Indexed;
import io.semla.persistence.annotations.Managed;
import javax.persistence.*;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Entity
@Managed
@NoArgsConstructor
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

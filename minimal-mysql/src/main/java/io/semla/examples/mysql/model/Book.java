package io.semla.examples.mysql.model;

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

  @NotNull
  public String name;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  public Author author;
}

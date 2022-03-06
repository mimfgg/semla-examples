package io.semla.examples.graphql.model;

import io.semla.persistence.annotations.Indexed;
import io.semla.persistence.annotations.Managed;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@Managed
@NoArgsConstructor
public class Author {

  @Id
  @GeneratedValue
  public int id;

  @Indexed
  @NotNull
  public String name;

  @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
  public List<Book> books;
}

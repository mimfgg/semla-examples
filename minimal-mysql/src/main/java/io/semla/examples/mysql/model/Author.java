package io.semla.examples.mysql.model;

import io.semla.persistence.annotations.Managed;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Managed
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {

  @Id
  @GeneratedValue
  public int id;

  @NotNull
  public String name;

  @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
  public List<Book> books;
}

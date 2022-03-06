package io.semla.examples.mysql.model;

import io.semla.persistence.annotations.Managed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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

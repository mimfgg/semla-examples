package io.semla.examples.mysql.model;

import io.semla.persistence.annotations.Managed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

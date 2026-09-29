package com.javanauta.cadastro_livro.infrastructure.entitys;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "livro")
@Entity

public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "Titulo", unique = true)
    private String titulo;

    @Column(name = "Autor", unique = true)
    private String autor;

    @Column(name = "Genero", unique = true)
    private String genero;

    @Column(name = "Paginas", unique = true)
    private String paginas;
}

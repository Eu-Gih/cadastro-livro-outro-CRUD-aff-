package com.javanauta.cadastro_livro.infrastructure.repository;

import com.javanauta.cadastro_livro.infrastructure.entitys.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    Optional<Livro> findByTitulo(String titulo);

    boolean existsByTitulo(String titulo);

    @Transactional
    void deleteByTitulo(String titulo);
}

package com.javanauta.cadastro_livro.business;

import com.javanauta.cadastro_livro.infrastructure.entitys.Livro;
import com.javanauta.cadastro_livro.infrastructure.repository.LivroRepository;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    private final LivroRepository repository;

    public LivroService(LivroRepository repository){
        this.repository = repository;
    }

    public void SalvarLivro(Livro livro){
        repository.saveAndFlush(livro);
    }

    public Livro buscarLivroPorTitulo(String titulo){
        return repository.findByTitulo(titulo).orElseThrow(
                () -> new RuntimeException("Titulo não encontrado!")
        );
    }

    public void deletarLivroPorTitulo(String titulo){
        repository.deleteByTitulo(titulo);
    }

    public void AtualizarLivroPorTitulo (String titulo, Livro livro){
        Livro livroEntity = repository.findByTitulo(titulo).orElseThrow(
                () -> new RuntimeException("Titulo não encontrado!"));

        Livro livroAtualizado = Livro.builder()
                .titulo(livro.getTitulo() != null ? livro.getTitulo() : livroEntity.getTitulo())
                .autor(livro.getAutor() != null ? livro.getAutor() : livroEntity.getAutor())
                .genero(livro.getGenero() != null ? livro.getGenero() : livroEntity.getGenero())
                .paginas(livro.getPaginas() != null ? livro.getPaginas() : livroEntity.getPaginas())
                .id(livroEntity.getId())
                .build();

        repository.saveAndFlush(livroAtualizado);

    }
}
